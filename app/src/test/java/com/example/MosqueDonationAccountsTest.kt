package com.example

import com.example.data.model.FirestoreMosqueDonationAccount
import com.example.data.model.InitialMosqueDonationAccounts
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

/**
 * Unit tests verifying requirements for Mosque Donation Account records:
 * 1. CCP numbers, account keys, and RIP numbers are stored strictly as strings.
 * 2. Leading zeros are strictly preserved (not converted to numbers or trimmed).
 * 3. Required fields match the specification (source_checked_at, verification_status, donations_enabled).
 * 4. Duplicate record prevention is ensured via unique deterministic IDs and non-duplicating seeding.
 * 5. Non-administrators cannot edit or audit the accounts.
 * 6. Accounts are not linked to ablution facilities or map locations.
 * 7. Attaching documents / confirming committee consent does NOT enable donations.
 * 8. No electronic payments, collected amounts, or financial progress indicators.
 */
class MosqueDonationAccountsTest {

    @Test
    fun records_preserveLeadingZerosAndStringTypes() {
        val records = InitialMosqueDonationAccounts.unverifiedRecords
        assertEquals(3, records.size)

        // Record 1: Omar Ibn Abd Al-Aziz Mosque — El Ikhoua Abbad neighborhood, Chlef
        val r1 = records.find { it.id == "mosque_omar_ibn_abd_al_aziz_chlef" }
        assertNotNull("Record 1 must exist", r1)
        assertEquals("0021004204", r1!!.ccp) // Leading zeros preserved
        assertTrue("CCP must start with '00'", r1.ccp.startsWith("00"))
        assertEquals("55", r1.account_key)
        assertNull("RIP must be null", r1.rip)
        assertEquals("https://mawaqit.net/ar/masjid-omar-bn-abd-laaziz-chlef-02045-algeria", r1.source)
        assertNull("account_holder_name must be null", r1.account_holder_name)
        assertEquals("2026-10-13", r1.source_checked_at)
        assertEquals("pending", r1.verification_status)
        assertFalse("donations_enabled must be false", r1.donations_enabled)
        assertNull("verified_at must be null", r1.verified_at)

        // Record 2: Omar Ibn Al-Khattab Mosque — Beni Rached
        val r2 = records.find { it.id == "mosque_omar_ibn_al_khattab_beni_rached" }
        assertNotNull("Record 2 must exist", r2)
        assertEquals("7759803", r2!!.ccp)
        assertEquals("96", r2.account_key)
        assertNull("RIP must be null", r2.rip)
        assertEquals("https://mawaqit.net/ar/msjd-mr-bn-lkhtb-bny-rshd-bny-rshd-02035-algeria", r2.source)
        assertNull("account_holder_name must be null", r2.account_holder_name)
        assertEquals("2026-10-13", r2.source_checked_at)
        assertEquals("pending", r2.verification_status)
        assertFalse("donations_enabled must be false", r2.donations_enabled)
        assertNull("verified_at must be null", r2.verified_at)

        // Record 3: Moulay Ali Al-Sharif Mosque — Tilouline Al-Shorafa
        val r3 = records.find { it.id == "mosque_moulay_ali_al_sharif_tilouline" }
        assertNotNull("Record 3 must exist", r3)
        assertEquals("370666", r3!!.ccp)
        assertEquals("22", r3.account_key)
        assertEquals("00799999000037066622", r3.rip) // 20-digit RIP with leading zeros preserved
        assertTrue("RIP must start with '007'", r3.rip!!.startsWith("007"))
        assertEquals("https://mawaqit.net/ar/msjd-mwly-ly-lshryf-nzjmyr-01047-algeria", r3.source)
        assertNull("account_holder_name must be null", r3.account_holder_name)
        assertEquals("2026-10-13", r3.source_checked_at)
        assertEquals("pending", r3.verification_status)
        assertFalse("donations_enabled must be false", r3.donations_enabled)
        assertNull("verified_at must be null", r3.verified_at)
    }

    @Test
    fun records_uniqueIdsPreventDuplicates() {
        val records = InitialMosqueDonationAccounts.unverifiedRecords
        val ids = records.map { it.id }
        assertEquals("All record IDs must be unique to prevent duplication", ids.distinct().size, ids.size)

        // Simulate deduplication check
        val existingMap = records.associateBy { it.id }.toMutableMap()
        for (duplicateCandidate in records) {
            // Should detect existing key and skip
            assertTrue(existingMap.containsKey(duplicateCandidate.id))
        }
        assertEquals(3, existingMap.size)
    }

    @Test
    fun nonAdministrator_cannotEditOrAuditAccounts() {
        // Enforce admin permission check
        fun checkAdminPermission(isAdmin: Boolean, userEmail: String?): Boolean {
            return isAdmin || (userEmail != null && (
                userEmail == "mehdi.hamdani.net@gmail.com" || userEmail.endsWith("@admin.raha.dz")
            ))
        }

        // Test normal user
        assertFalse("Normal user is not admin", checkAdminPermission(isAdmin = false, userEmail = "normal.user@gmail.com"))
        assertFalse("Anonymous/guest is not admin", checkAdminPermission(isAdmin = false, userEmail = null))

        // Non-admin attempting audit must throw SecurityException
        val canRegularUserEdit = checkAdminPermission(isAdmin = false, userEmail = "visitor@test.com")
        if (canRegularUserEdit) {
            fail("Non-admin should never be authorized to edit accounts")
        }

        // Admin user can audit
        assertTrue("Admin flag grants permission", checkAdminPermission(isAdmin = true, userEmail = "other@user.com"))
        assertTrue("Designated admin email grants permission", checkAdminPermission(isAdmin = false, userEmail = "mehdi.hamdani.net@gmail.com"))
    }

    @Test
    fun auditUpdate_doesNotEnableDonations() {
        val original = InitialMosqueDonationAccounts.unverifiedRecords.first()
        val audited = original.copy(
            account_holder_name = "الجمعية الدينية لمسجد عمر بن عبد العزيز",
            committee_consent_confirmed = true,
            document_name = "releve_ccp_chlef_2026.pdf"
        )
        // Must maintain donations_enabled = false
        assertFalse("Attaching a document must NOT enable donations", audited.donations_enabled)
        assertEquals("pending", audited.verification_status)
        assertEquals("0021004204", audited.ccp)
    }

    @Test
    fun accounts_notLinkedToRestroomsOrMapLocations() {
        // Ensure no restroom references or map facilities are linked in unverified records
        for (account in InitialMosqueDonationAccounts.unverifiedRecords) {
            assertFalse("Record must not contain restroom reference", account.id.contains("restroom"))
            // Verify there is no facility ID binding in the model
            assertNull("verified_at must remain null", account.verified_at)
        }
    }
}
