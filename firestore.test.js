const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";
const ADMIN_UID = "admin_mehdi_789";
const ADMIN_EMAIL = "mehdi.hamdani.net@gmail.com";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

// --- TESTS ---

test("Unauthenticated: cannot read restrooms", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("restrooms").get());
});

test("Authenticated: can read restrooms", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("restrooms").get());
});

test("Authenticated: can create restroom with matching createdBy", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("restrooms").doc("r1").set({
      id: "r1",
      name: "Public Restroom",
      nameAr: "دورة مياه عمومية",
      latitude: 36.75,
      longitude: 3.05,
      province: "الجزائر",
      createdBy: ALICE_UID,
      createdAt: new Date(),
    })
  );
});

test("Authenticated: cannot create restroom with mismatched createdBy", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(
    aliceDb.collection("restrooms").doc("r2").set({
      id: "r2",
      name: "Public Restroom",
      latitude: 36.75,
      longitude: 3.05,
      province: "الجزائر",
      createdBy: BOB_UID,
      createdAt: new Date(),
    })
  );
});

test("Authenticated: can create review with matching userId", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("reviews").doc("rev1").set({
      id: "rev1",
      restroomId: "r1",
      userId: ALICE_UID,
      authorName: "Alice",
      rating: 4.5,
      cleanlinessRating: 4.0,
      comment: "نظيف جدا",
      hasWaterAvailable: true,
      hasSoapPaper: true,
      createdAt: new Date(),
    })
  );
});

test("Authenticated: cannot update another user's review", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("reviews").doc("rev_bob").set({
      id: "rev_bob",
      restroomId: "r1",
      userId: BOB_UID,
      authorName: "Bob",
      rating: 3.0,
      cleanlinessRating: 3.0,
      createdAt: new Date(),
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(
    aliceDb.collection("reviews").doc("rev_bob").update({
      comment: "Hacked comment",
    })
  );
});

test("User isolation: Alice can access her own profile and favorites, but not Bob's", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice",
      email: "alice@example.com",
      createdAt: new Date(),
    })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("favorites")
      .doc("r1")
      .set({
        restroomId: "r1",
        addedAt: new Date(),
      })
  );

  // Alice cannot read Bob's profile
  await assertFails(aliceDb.collection("users").doc(BOB_UID).get());
});

// --- MOSQUE DONATION ACCOUNTS TESTS ---

test("Non-administrator: cannot read mosque donation accounts", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@example.com" }).firestore();
  await assertFails(aliceDb.collection("mosque_donation_accounts").get());
  await assertFails(aliceDb.collection("mosque_donation_accounts").doc("mosque_omar_chlef").get());
});

test("Non-administrator: cannot create or edit mosque donation accounts", async () => {
  const bobDb = testEnv.authenticatedContext(BOB_UID, { email: "bob@example.com" }).firestore();
  await assertFails(
    bobDb.collection("mosque_donation_accounts").doc("mosque_omar_chlef").set({
      id: "mosque_omar_chlef",
      mosque_name: "Omar Ibn Abd Al-Aziz Mosque",
      ccp: "0021004204",
      account_key: "55",
      rip: null,
      source: "https://mawaqit.net/ar/masjid-omar-bn-abd-laaziz-chlef-02045-algeria",
      account_holder_name: null,
      source_checked_at: "2026-10-13",
      verification_status: "pending",
      donations_enabled: false,
      verified_at: null,
    })
  );
});

test("Administrator: can read, create, and audit mosque donation accounts with leading zeros preserved", async () => {
  const adminDb = testEnv.authenticatedContext(ADMIN_UID, { email: ADMIN_EMAIL }).firestore();

  const record1 = {
    id: "mosque_omar_chlef",
    mosque_name: "Omar Ibn Abd Al-Aziz Mosque",
    location_description: "El Ikhoua Abbad neighborhood, Chlef",
    ccp: "0021004204", // leading zeros preserved as string
    account_key: "55",
    rip: null,
    source: "https://mawaqit.net/ar/masjid-omar-bn-abd-laaziz-chlef-02045-algeria",
    account_holder_name: null,
    source_checked_at: "2026-10-13",
    verification_status: "pending",
    donations_enabled: false,
    verified_at: null,
    committee_consent_confirmed: false,
    document_name: null,
    created_at: new Date(),
  };

  await assertSucceeds(
    adminDb.collection("mosque_donation_accounts").doc("mosque_omar_chlef").set(record1)
  );

  // Admin can read back
  const docSnap = await adminDb.collection("mosque_donation_accounts").doc("mosque_omar_chlef").get();
  if (docSnap.data().ccp !== "0021004204") {
    throw new Error("Leading zeros in CCP were altered!");
  }

  // Admin can update audit info
  await assertSucceeds(
    adminDb.collection("mosque_donation_accounts").doc("mosque_omar_chlef").update({
      account_holder_name: "Association Religieuse Mosquée Omar Ibn Abd Al-Aziz",
      committee_consent_confirmed: true,
      document_name: "releve_ccp_chlef_2026.pdf",
      admin_notes: "Checked official stamp from Religious Affairs direction.",
    })
  );
});
