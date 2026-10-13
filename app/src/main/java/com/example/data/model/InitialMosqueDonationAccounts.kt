package com.example.data.model

import com.google.firebase.Timestamp

/**
 * Hardcoded initial mosque donation account records provided for review only.
 * IMPORTANT:
 * - CCP, account keys, and RIPs are stored strictly as Strings to preserve leading zeros.
 * - Missing values are null; no numbers are derived or invented.
 * - donations_enabled is false.
 * - verification_status is "pending".
 * - source_checked_at indicates when the source was reviewed (2026-10-13), NOT when verified.
 */
object InitialMosqueDonationAccounts {
    val unverifiedRecords: List<FirestoreMosqueDonationAccount> = listOf(
        FirestoreMosqueDonationAccount(
            id = "mosque_omar_ibn_abd_al_aziz_chlef",
            mosque_name = "مسجد عمر بن عبد العزيز (Omar Ibn Abd Al-Aziz Mosque)",
            location_description = "حي الإخوة عباد، ولاية الشلف (El Ikhoua Abbad neighborhood, Chlef)",
            ccp = "0021004204",
            account_key = "55",
            rip = null,
            source = "https://mawaqit.net/ar/masjid-omar-bn-abd-laaziz-chlef-02045-algeria",
            account_holder_name = null,
            source_checked_at = "2026-10-13",
            verification_status = "pending",
            donations_enabled = false,
            verified_at = null,
            committee_consent_confirmed = false,
            document_name = null,
            document_attached_at = null,
            admin_notes = "حساب منشور عبر الإنترنت (Mawaqit) - غير مؤكد من لجنة المسجد أو بريد الجزائر، قيد المراجعة فقط."
        ),
        FirestoreMosqueDonationAccount(
            id = "mosque_omar_ibn_al_khattab_beni_rached",
            mosque_name = "مسجد عمر بن الخطاب (Omar Ibn Al-Khattab Mosque)",
            location_description = "بني راشد، ولاية الشلف (Beni Rached)",
            ccp = "7759803",
            account_key = "96",
            rip = null,
            source = "https://mawaqit.net/ar/msjd-mr-bn-lkhtb-bny-rshd-bny-rshd-02035-algeria",
            account_holder_name = null,
            source_checked_at = "2026-10-13",
            verification_status = "pending",
            donations_enabled = false,
            verified_at = null,
            committee_consent_confirmed = false,
            document_name = null,
            document_attached_at = null,
            admin_notes = "حساب منشور عبر الإنترنت (Mawaqit) - قيد المراجعة الإدارية للتأكد من هوية المستفيد واللجنة."
        ),
        FirestoreMosqueDonationAccount(
            id = "mosque_moulay_ali_al_sharif_tilouline",
            mosque_name = "مسجد مولاي علي الشريف (Moulay Ali Al-Sharif Mosque)",
            location_description = "تيلولين الشرفاء، أنزجمير، ولاية أدرار (Tilouline Al-Shorafa)",
            ccp = "370666",
            account_key = "22",
            rip = "00799999000037066622",
            source = "https://mawaqit.net/ar/msjd-mwly-ly-lshryf-nzjmyr-01047-algeria",
            account_holder_name = null,
            source_checked_at = "2026-10-13",
            verification_status = "pending",
            donations_enabled = false,
            verified_at = null,
            committee_consent_confirmed = false,
            document_name = null,
            document_attached_at = null,
            admin_notes = "حساب منشور عبر الإنترنت (Mawaqit) مع RIP كامل - قيد التحقق والتدقيق الإداري."
        )
    )
}
