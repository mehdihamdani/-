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
  const adminDb = testEnv.authenticatedContext(ADMIN_UID, { email: ADMIN_EMAIL, email_verified: true }).firestore();

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

test("Authenticated: can create mosque with Quran school and Facebook URL", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("restrooms").doc("mosque_algiers_1").set({
      id: "mosque_algiers_1",
      name: "Grande Mosquée d'Alger",
      nameAr: "جامع الجزائر الأعظم",
      latitude: 36.7345,
      longitude: 3.1418,
      province: "الجزائر العاصمة",
      type: "mosque",
      hasQuranSchool: true,
      hasFridayPrayer: true,
      hasLibrary: true,
      facebookUrl: "https://www.facebook.com/DjamaaElDjazairOfficial",
      imamName: "الشيخ محمد المأمون القاسمي",
      mosqueServicesNotes: "مدرسة قرآنية لتحفيظ القرآن الكريم يومياً بعد صلاة العصر",
      createdBy: ALICE_UID,
      createdAt: new Date(),
    })
  );
});

test("Authenticated: can create and update hammam with services, schedules, and Facebook URL", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("restrooms").doc("hammam_righa_1").set({
      id: "hammam_righa_1",
      name: "Station Thermale Hammam Righa",
      nameAr: "المركب الحموي المعدني حمام ريغة",
      latitude: 36.3792,
      longitude: 2.3967,
      province: "عين الدفلى",
      type: "hammam",
      isFree: false,
      priceDzd: 400,
      hasSteamSauna: true,
      hasKessaScrub: true,
      hasPrivateShowerRooms: true,
      isThermalSpring: true,
      hasTowelRental: true,
      hasTeaLounge: true,
      hasBarberGrooming: false,
      hammamPhone: "027 59 10 20",
      hammamMenHours: "يومياً 06:00 - 21:00 (جناح مستقل)",
      hammamWomenHours: "يومياً 08:00 - 19:00 (جناح مستقل + غرف عائلية)",
      hammamServicesNotes: "مياه معدنية علاجية ساخنة (67°م)، غرف استشفاء فردية وعائلية، تكييس وتدليك طبيعي.",
      facebookUrl: "https://www.facebook.com/StationThermaleHammamRigha",
      createdBy: ALICE_UID,
      createdAt: new Date(),
    })
  );

  // Another authenticated community contributor (Bob) can update Hammam services & hours,
  // but cannot tamper with createdBy or priceDzd
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertSucceeds(
    bobDb.collection("restrooms").doc("hammam_righa_1").update({
      hasBarberGrooming: true,
      hammamServicesNotes: "تمت إضافة صالون حلاقة وعناية داخل المركب الحموي.",
      updatedAt: new Date(),
    })
  );

  await assertFails(
    bobDb.collection("restrooms").doc("hammam_righa_1").update({
      createdBy: BOB_UID,
    })
  );
});
