package com.example.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.data.model.AppLanguage

val LocalAppLanguage = staticCompositionLocalOf { AppLanguage.AR }

/**
 * Complete Localization Dictionary supporting Arabic, French, and English
 * for all screens, dialogs, cards, filters, and buttons in the RAHA DZ application.
 */
object AppStrings {

    // --- NAVIGATION & COMMON ---
    fun navMap(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الخريطة"
        AppLanguage.FR -> "Carte"
        AppLanguage.EN -> "Map"
    }

    fun navRestrooms(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دورات المياه"
        AppLanguage.FR -> "Toilettes"
        AppLanguage.EN -> "Restrooms"
    }

    fun navFavorites(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "المفضلة"
        AppLanguage.FR -> "Favoris"
        AppLanguage.EN -> "Favorites"
    }

    fun navDonations(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التبرعات"
        AppLanguage.FR -> "Dons"
        AppLanguage.EN -> "Donations"
    }

    fun navSettings(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الإعدادات"
        AppLanguage.FR -> "Paramètres"
        AppLanguage.EN -> "Settings"
    }

    fun navProfile(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الملف الشخصي"
        AppLanguage.FR -> "Profil"
        AppLanguage.EN -> "Profile"
    }

    fun appName(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "راحة"
        AppLanguage.FR -> "Raha"
        AppLanguage.EN -> "Raha"
    }

    fun appSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دليلك الذكي لدورات المياه ومرافق الوضوء بالجزائر"
        AppLanguage.FR -> "Votre guide des sanitaires et ablutions en Algérie"
        AppLanguage.EN -> "Your smart guide for restrooms and ablutions in Algeria"
    }

    fun back(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الرجوع"
        AppLanguage.FR -> "Retour"
        AppLanguage.EN -> "Back"
    }

    fun close(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إغلاق"
        AppLanguage.FR -> "Fermer"
        AppLanguage.EN -> "Close"
    }

    fun cancel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إلغاء"
        AppLanguage.FR -> "Annuler"
        AppLanguage.EN -> "Cancel"
    }

    fun save(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "حفظ"
        AppLanguage.FR -> "Enregistrer"
        AppLanguage.EN -> "Save"
    }

    fun add(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إضافة"
        AppLanguage.FR -> "Ajouter"
        AppLanguage.EN -> "Add"
    }

    fun edit(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تعديل"
        AppLanguage.FR -> "Modifier"
        AppLanguage.EN -> "Edit"
    }

    fun delete(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "حذف"
        AppLanguage.FR -> "Supprimer"
        AppLanguage.EN -> "Delete"
    }

    fun open(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "فتح"
        AppLanguage.FR -> "Ouvrir"
        AppLanguage.EN -> "Open"
    }

    fun view(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "عرض"
        AppLanguage.FR -> "Voir"
        AppLanguage.EN -> "View"
    }

    fun copy(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "نسخ"
        AppLanguage.FR -> "Copier"
        AppLanguage.EN -> "Copy"
    }

    fun copied(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تم النسخ بنجاح"
        AppLanguage.FR -> "Copié avec succès"
        AppLanguage.EN -> "Copied successfully"
    }

    fun free(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مجاني"
        AppLanguage.FR -> "Gratuit"
        AppLanguage.EN -> "Free"
    }

    fun dzdCurrency(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دج"
        AppLanguage.FR -> "DZD"
        AppLanguage.EN -> "DZD"
    }

    fun searchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ابحث بالولاية (مثلاً: وهران، سطيف...) أو الحي"
        AppLanguage.FR -> "Rechercher par wilaya (ex: Oran, Sétif) ou quartier"
        AppLanguage.EN -> "Search by province (e.g. Oran, Setif) or district"
    }

    fun yourLocation(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "موقعك"
        AppLanguage.FR -> "Votre position"
        AppLanguage.EN -> "Your location"
    }

    fun savedLocally(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "محفوظ محلياً"
        AppLanguage.FR -> "Stocké localement"
        AppLanguage.EN -> "Saved locally"
    }

    // --- TOP BAR & HOMESCREEN BANNERS ---
    fun offlineBanner(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "وضع عدم الاتصال: $count مرافق مخزنة محلياً في Room"
        AppLanguage.FR -> "Mode hors-ligne : $count lieux enregistrés localement"
        AppLanguage.EN -> "Offline mode: $count places stored locally in Room"
    }

    fun manageStorage(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إدارة التخزين"
        AppLanguage.FR -> "Gérer le stockage"
        AppLanguage.EN -> "Manage storage"
    }

    fun nearestToYou(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الأقرب إليك الآن"
        AppLanguage.FR -> "Le plus proche de vous"
        AppLanguage.EN -> "Nearest to you now"
    }

    fun awayOnly(distance: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "يبعد فقط $distance"
        AppLanguage.FR -> "À seulement $distance"
        AppLanguage.EN -> "Only $distance away"
    }

    fun navigate(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "انطلق"
        AppLanguage.FR -> "Y aller"
        AppLanguage.EN -> "Navigate"
    }

    // --- FILTER CHIPS ---
    fun filterAll(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الكل"
        AppLanguage.FR -> "Tous"
        AppLanguage.EN -> "All"
    }

    fun filterMosques(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مساجد"
        AppLanguage.FR -> "Mosquées"
        AppLanguage.EN -> "Mosques"
    }

    fun filterMusallas(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مصليات"
        AppLanguage.FR -> "Salles de prière"
        AppLanguage.EN -> "Prayer rooms"
    }

    fun filterHammams(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "حمامات شعبية / دوش"
        AppLanguage.FR -> "Hammams / Douches"
        AppLanguage.EN -> "Baths / Showers"
    }

    fun filterNaftal(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "نفطال / طريق سريع"
        AppLanguage.FR -> "Naftal / Autoroute"
        AppLanguage.EN -> "Naftal / Highway"
    }

    fun filterTransport(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "محطات نقل وسوجرال"
        AppLanguage.FR -> "Gares & Transports"
        AppLanguage.EN -> "Transport Stations"
    }

    fun filterMalls(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مراكز تجارية"
        AppLanguage.FR -> "Centres commerciaux"
        AppLanguage.EN -> "Shopping malls"
    }

    fun filterPrivate(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دورات مياه خواص"
        AppLanguage.FR -> "Toilettes privées"
        AppLanguage.EN -> "Private restrooms"
    }

    fun filterFreeOnly(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مجاني فقط"
        AppLanguage.FR -> "Gratuit uniquement"
        AppLanguage.EN -> "Free only"
    }

    fun filterWomenOnly(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مخصص للنساء"
        AppLanguage.FR -> "Section femmes"
        AppLanguage.EN -> "Women section"
    }

    fun filterPmr(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ذوي الهمم (PMR)"
        AppLanguage.FR -> "Accès PMR / Handicap"
        AppLanguage.EN -> "Wheelchair accessible"
    }

    fun filterCleanliness(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "✨ نظافة عالية"
        AppLanguage.FR -> "✨ Très propre"
        AppLanguage.EN -> "✨ High cleanliness"
    }

    fun filterBabyChanging(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "👶 طاولة رضع"
        AppLanguage.FR -> "👶 Table à langer"
        AppLanguage.EN -> "👶 Baby changing"
    }

    fun filterShower(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دوش ساخن"
        AppLanguage.FR -> "Douche chaude"
        AppLanguage.EN -> "Hot shower"
    }

    // --- EMPTY STATES ---
    fun noResultsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "لا توجد نتائج مطابقة للبحث"
        AppLanguage.FR -> "Aucun résultat trouvé"
        AppLanguage.EN -> "No matching results found"
    }

    fun noResultsSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "جرّب تغيير خيارات الفلترة أو اختر ولاية أخرى أو ساهم بإضافة مرفق جديد."
        AppLanguage.FR -> "Modifiez vos filtres, choisissez une autre wilaya ou ajoutez un nouveau lieu."
        AppLanguage.EN -> "Try changing your filters, selecting another province, or adding a new location."
    }

    fun emptyFavoritesTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "قائمة المفضلة فارغة"
        AppLanguage.FR -> "Aucun favori enregistré"
        AppLanguage.EN -> "Favorites list is empty"
    }

    fun emptyFavoritesSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "انقر على رمز القلب في أي دورة مياه أو حمام لإضافتها إلى قائمة المفضلة والوصول إليها بسرعة."
        AppLanguage.FR -> "Touchez l'icône cœur sur n'importe quel lieu pour l'enregistrer dans vos favoris."
        AppLanguage.EN -> "Tap the heart icon on any restroom or bath to save it to your favorites for quick access."
    }

    // --- RESTROOM CARD & AMENITIES ---
    fun waterAvailable(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ماء متوفر"
        AppLanguage.FR -> "Eau disponible"
        AppLanguage.EN -> "Water available"
    }

    fun waterCut(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "انقطاع الماء"
        AppLanguage.FR -> "Coupure d'eau"
        AppLanguage.EN -> "Water cut"
    }

    fun soapAvailable(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "صابون ومحارم"
        AppLanguage.FR -> "Savon & Papier"
        AppLanguage.EN -> "Soap & Paper"
    }

    fun filterSoapPaper(lang: AppLanguage): String = soapAvailable(lang)

    fun wuduAvailable(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "وضوء مهيأ"
        AppLanguage.FR -> "Espace ablutions"
        AppLanguage.EN -> "Wudu area"
    }

    fun cleanliness(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "النظافة"
        AppLanguage.FR -> "Propreté"
        AppLanguage.EN -> "Cleanliness"
    }

    fun reviewsCount(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "($count تقييم)"
        AppLanguage.FR -> "($count avis)"
        AppLanguage.EN -> "($count reviews)"
    }

    // --- RESTROOM DETAIL SHEET ---
    fun detailsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تفاصيل المرفق"
        AppLanguage.FR -> "Détails du lieu"
        AppLanguage.EN -> "Facility details"
    }

    fun openingHours(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "أوقات العمل"
        AppLanguage.FR -> "Horaires d'ouverture"
        AppLanguage.EN -> "Opening hours"
    }

    fun address(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "العنوان"
        AppLanguage.FR -> "Adresse"
        AppLanguage.EN -> "Address"
    }

    fun notes(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ملاحظات إضافية"
        AppLanguage.FR -> "Notes complémentaires"
        AppLanguage.EN -> "Additional notes"
    }

    fun reportWaterCut(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تبليغ عن انقطاع الماء"
        AppLanguage.FR -> "Signaler coupure d'eau"
        AppLanguage.EN -> "Report water cut"
    }

    fun reportWaterRestored(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تأكيد عودة الماء"
        AppLanguage.FR -> "Confirmer retour de l'eau"
        AppLanguage.EN -> "Confirm water restored"
    }

    fun shareFacility(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مشاركة المرفق"
        AppLanguage.FR -> "Partager le lieu"
        AppLanguage.EN -> "Share location"
    }

    fun openInMaps(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "فتح في خرائط Google"
        AppLanguage.FR -> "Ouvrir dans Google Maps"
        AppLanguage.EN -> "Open in Google Maps"
    }

    fun mosqueCcpNotice(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "حساب التبرع لصيانة مرافق المسجد (CCP)"
        AppLanguage.FR -> "Compte CCP pour l'entretien de la mosquée"
        AppLanguage.EN -> "CCP donation account for mosque maintenance"
    }

    fun reviewsSection(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تقييمات المسافرين والمجتمع ($count)"
        AppLanguage.FR -> "Avis des voyageurs et communauté ($count)"
        AppLanguage.EN -> "Traveler & Community Reviews ($count)"
    }

    fun writeReview(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "أضف تقييمك وتجربتك"
        AppLanguage.FR -> "Laisser un avis"
        AppLanguage.EN -> "Write a review"
    }

    fun yourName(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "اسمك أو لقبك *"
        AppLanguage.FR -> "Votre nom ou pseudo *"
        AppLanguage.EN -> "Your name or nickname *"
    }

    fun overallScore(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التقييم العام"
        AppLanguage.FR -> "Note globale"
        AppLanguage.EN -> "Overall rating"
    }

    fun cleanlinessScore(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مستوى النظافة"
        AppLanguage.FR -> "Niveau de propreté"
        AppLanguage.EN -> "Cleanliness level"
    }

    fun writeCommentPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "اكتب ملاحظاتك حول النظافة، توفر الماء، والخدمة..."
        AppLanguage.FR -> "Écrivez vos remarques sur la propreté, l'eau, les services..."
        AppLanguage.EN -> "Write your feedback on cleanliness, water, services..."
    }

    fun submitReview(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "نشر التقييم"
        AppLanguage.FR -> "Publier l'avis"
        AppLanguage.EN -> "Submit review"
    }

    fun noReviewsYet(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "لا توجد تقييمات بعد. كن أول من يقيّم!"
        AppLanguage.FR -> "Aucun avis pour l'instant. Soyez le premier !"
        AppLanguage.EN -> "No reviews yet. Be the first to review!"
    }

    fun attachPhoto(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إرفاق صورة للمرفق"
        AppLanguage.FR -> "Ajouter une photo du lieu"
        AppLanguage.EN -> "Attach facility photo"
    }

    fun takePhotoCamera(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التقاط بالكاميرا"
        AppLanguage.FR -> "Prendre une photo"
        AppLanguage.EN -> "Take photo with camera"
    }

    fun pickFromGallery(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "اختيار من المعرض"
        AppLanguage.FR -> "Galerie photos"
        AppLanguage.EN -> "Choose from gallery"
    }

    fun photoAttached(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تم إرفاق صورة المرفق بنجاح"
        AppLanguage.FR -> "Photo attachée avec succès"
        AppLanguage.EN -> "Photo attached successfully"
    }

    fun cameraPermissionRequired(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "يلزم إذن الكاميرا لالتقاط صور لمرافق دورات المياه."
        AppLanguage.FR -> "Permission caméra requise pour prendre des photos."
        AppLanguage.EN -> "Camera permission required to take photos of restrooms."
    }

    // --- ADD RESTROOM DIALOG ---
    fun addDialogTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إضافة دورة مياه أو مرفق وضوء"
        AppLanguage.FR -> "Ajouter un sanitaire ou lieu d'ablution"
        AppLanguage.EN -> "Add Restroom or Ablution Facility"
    }

    fun addDialogSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ساهم بإضافة دورة مياه عمومية أو خاصة لمساعدة المسافرين عبر الـ 69 ولاية"
        AppLanguage.FR -> "Contribuez en ajoutant des sanitaires pour aider les voyageurs à travers les 69 wilayas"
        AppLanguage.EN -> "Contribute by adding restrooms to help travelers across all 69 provinces"
    }

    fun nameArabicLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "اسم المرفق أو المسجد (بالعربية) *"
        AppLanguage.FR -> "Nom du lieu (en arabe) *"
        AppLanguage.EN -> "Facility name (Arabic) *"
    }

    fun nameFrenchLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "اسم المرفق بالفرنسية (اختياري)"
        AppLanguage.FR -> "Nom du lieu (en français) *"
        AppLanguage.EN -> "Facility name (French/English)"
    }

    fun wilayaLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الولاية (69 ولاية) *"
        AppLanguage.FR -> "Wilaya (69 wilayas) *"
        AppLanguage.EN -> "Province (69 wilayas) *"
    }

    fun communeLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "البلدية أو الحي *"
        AppLanguage.FR -> "Commune ou quartier *"
        AppLanguage.EN -> "Municipality or district *"
    }

    fun facilityTypeLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "نوع المرفق *"
        AppLanguage.FR -> "Type d'établissement *"
        AppLanguage.EN -> "Facility type *"
    }

    fun isFreeQuestion(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "هل المرفق مجاني؟"
        AppLanguage.FR -> "L'accès est-il gratuit ?"
        AppLanguage.EN -> "Is access free?"
    }

    fun priceLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "سعر الدخول (بالدينار الجزائري دج)"
        AppLanguage.FR -> "Tarif d'entrée (en dinars DZD)"
        AppLanguage.EN -> "Entry fee (in Algerian Dinars DZD)"
    }

    fun availableAmenities(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التجهيزات والخدمات المتوفرة"
        AppLanguage.FR -> "Équipements & Commodités disponibles"
        AppLanguage.EN -> "Available Amenities & Features"
    }

    fun submitContribution(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إرسال وإضافة للمجتمع"
        AppLanguage.FR -> "Ajouter et partager"
        AppLanguage.EN -> "Submit and share"
    }

    // --- SETTINGS SCREEN ---
    fun settingsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إعدادات التطبيق"
        AppLanguage.FR -> "Paramètres de l'Application"
        AppLanguage.EN -> "App Settings"
    }

    fun settingsSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "خصص مظهر التطبيق، الألوان، الوضع الليلي واللغة حسب رغبتك"
        AppLanguage.FR -> "Personnalisez l'affichage, les couleurs, le thème sombre et la langue"
        AppLanguage.EN -> "Customize appearance, color themes, dark mode, and language"
    }

    fun themesSection(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ألوان وثيمات التطبيق (5 ثيمات)"
        AppLanguage.FR -> "Thèmes & Couleurs (5 thèmes)"
        AppLanguage.EN -> "Color Themes (5 themes)"
    }

    fun darkModeSection(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مظهر العرض والوضع الليلي"
        AppLanguage.FR -> "Affichage & Mode Sombre"
        AppLanguage.EN -> "Display & Dark Mode"
    }

    fun languageSection(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "لغة التطبيق (3 لغات)"
        AppLanguage.FR -> "Langue de l'Application (3 langues)"
        AppLanguage.EN -> "App Language (3 languages)"
    }

    fun appDataAndTools(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "بيانات التطبيق وأدوات النظام"
        AppLanguage.FR -> "Données & Outils système"
        AppLanguage.EN -> "App Data & System Tools"
    }

    fun offlineRoomStorage(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التخزين المحلي Room بدون إنترنت"
        AppLanguage.FR -> "Stockage local Room (Hors-ligne)"
        AppLanguage.EN -> "Offline Local Storage (Room)"
    }

    fun offlineRoomStorageSub(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إدارة البيانات والمزامنة غير المتصلة"
        AppLanguage.FR -> "Gestion des données et synchronisation"
        AppLanguage.EN -> "Data management and offline sync"
    }

    fun feasibilityStudy(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دراسة الجدوى الشاملة للمشروع"
        AppLanguage.FR -> "Étude de faisabilité du projet"
        AppLanguage.EN -> "Project Feasibility Study"
    }

    fun feasibilityStudySub(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "النموذج الاقتصادي والاستدامة بالجزائر"
        AppLanguage.FR -> "Modèle économique et impact en Algérie"
        AppLanguage.EN -> "Economic model & sustainability in Algeria"
    }

    // --- USER PROFILE SCREEN ---
    fun profileTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الملف الشخصي والحساب"
        AppLanguage.FR -> "Profil & Compte"
        AppLanguage.EN -> "Profile & Account"
    }

    fun adminBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مدير النظام"
        AppLanguage.FR -> "Administrateur"
        AppLanguage.EN -> "Administrator"
    }

    fun memberBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "عضو مساهم"
        AppLanguage.FR -> "Membre contributeur"
        AppLanguage.EN -> "Contributing member"
    }

    fun statAddedPlaces(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "المرافق المضافة"
        AppLanguage.FR -> "Lieux ajoutés"
        AppLanguage.EN -> "Added facilities"
    }

    fun statReviews(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التقييمات"
        AppLanguage.FR -> "Avis donnés"
        AppLanguage.EN -> "Reviews"
    }

    fun statFavorites(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "المفضلة"
        AppLanguage.FR -> "Favoris"
        AppLanguage.EN -> "Favorites"
    }

    fun signOut(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تسجيل الخروج"
        AppLanguage.FR -> "Se déconnecter"
        AppLanguage.EN -> "Sign out"
    }

    fun editProfile(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تعديل البيانات"
        AppLanguage.FR -> "Modifier le profil"
        AppLanguage.EN -> "Edit profile"
    }

    fun fullName(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الاسم الكامل"
        AppLanguage.FR -> "Nom complet"
        AppLanguage.EN -> "Full name"
    }

    fun phoneNumber(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "رقم الهاتف"
        AppLanguage.FR -> "Numéro de téléphone"
        AppLanguage.EN -> "Phone number"
    }

    fun myContributions(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مساهماتي المضافة"
        AppLanguage.FR -> "Mes contributions"
        AppLanguage.EN -> "My contributions"
    }

    fun noContributionsYet(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "لم تقم بإضافة أي دورات مياه بعد. ساهم بإضافة أول مرفق!"
        AppLanguage.FR -> "Vous n'avez pas encore ajouté de sanitaires. Contribuez dès maintenant !"
        AppLanguage.EN -> "You haven't added any restrooms yet. Add your first facility!"
    }

    // --- DONATIONS SCREEN ---
    fun donationTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التبرعات ودعم المبادرة"
        AppLanguage.FR -> "Dons & Soutien de l'initiative"
        AppLanguage.EN -> "Donations & Initiative Support"
    }

    fun tabAppDonation(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دعم تطبيق راحة 💙"
        AppLanguage.FR -> "Soutenir l'Appli 💙"
        AppLanguage.EN -> "Support App 💙"
    }

    fun tabMosqueDonation(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "صيانة دورات المساجد 🕌"
        AppLanguage.FR -> "Entretien Mosquées 🕌"
        AppLanguage.EN -> "Mosque Facilities 🕌"
    }

    fun donationNotice(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تطبيق راحة مبادرة تطوعية مجانية لخدمة المواطنين والمسافرين في 69 ولاية. تبرعك يساهم في تغطية تكاليف الخوادم وقواعد البيانات وتطوير الخرائط."
        AppLanguage.FR -> "L'application Raha est une initiative bénévole gratuite au service des citoyens et voyageurs dans 69 wilayas. Votre soutien permet de couvrir les serveurs et l'amélioration continue."
        AppLanguage.EN -> "Raha is a free voluntary initiative serving citizens and travelers across 69 provinces. Your donation covers server costs and map enhancements."
    }

    fun mosqueDonationNotice(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "حسابات بريدية رسمية ومباشرة لدعم لجان المساجد في صيانة مرافق الوضوء وتوفير الماء والمنظفات لخدمة بيوت الله والمصلين."
        AppLanguage.FR -> "Comptes CCP directs pour soutenir les comités des mosquées dans l'entretien des sanitaires, l'eau et les produits d'hygiène."
        AppLanguage.EN -> "Direct CCP postal accounts supporting mosque committees in maintaining ablution areas, water supply, and hygiene."
    }

    // --- MAP CONTROLS & FLOATING BAR ---
    fun showAllCategories(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "عرض الكل"
        AppLanguage.FR -> "Tout afficher"
        AppLanguage.EN -> "Show all"
    }

    fun activeLocationsCount(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "$count موقع ظاهر"
        AppLanguage.FR -> "$count lieux affichés"
        AppLanguage.EN -> "$count locations visible"
    }

    fun popularWilayas(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الولايات والمدن الشائعة"
        AppLanguage.FR -> "Wilayas & Villes populaires"
        AppLanguage.EN -> "Popular Provinces & Cities"
    }

    fun searchResults(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "نتائج البحث"
        AppLanguage.FR -> "Résultats de recherche"
        AppLanguage.EN -> "Search results"
    }

    // --- MOSQUE ADMIN AUDIT DIALOG ---
    fun adminReviewTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تدقيق حسابات التبرع للمساجد"
        AppLanguage.FR -> "Vérification des comptes de dons des mosquées"
        AppLanguage.EN -> "Mosque Donation Accounts Audit"
    }

    fun adminReviewSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "قسم مخصص للإدارة للمراجعة فقط • غير متاح للعامة"
        AppLanguage.FR -> "Section réservée aux administrateurs pour examen • Non public"
        AppLanguage.EN -> "Administrator review section only • Not accessible to public"
    }

    fun pendingStatus(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "قيد المراجعة والتدقيق"
        AppLanguage.FR -> "En attente de vérification"
        AppLanguage.EN -> "Pending verification"
    }

    fun sourceLink(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "رابط المصدر"
        AppLanguage.FR -> "Lien source"
        AppLanguage.EN -> "Source link"
    }

    fun auditAndAttachDoc(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تدقيق وإرفاق مستند"
        AppLanguage.FR -> "Vérifier & Joindre doc"
        AppLanguage.EN -> "Audit & Attach doc"
    }

    // --- ADDITIONAL SETTINGS STRINGS ---
    fun selected(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "محدد"
        AppLanguage.FR -> "Sélectionné"
        AppLanguage.EN -> "Selected"
    }

    fun openButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "فتح ←"
        AppLanguage.FR -> "Ouvrir →"
        AppLanguage.EN -> "Open →"
    }

    fun viewButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "عرض ←"
        AppLanguage.FR -> "Voir →"
        AppLanguage.EN -> "View →"
    }

    fun appVersionInfo(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "راحة (RAHA) - الإصدار 2.4.0 🇩🇿"
        AppLanguage.FR -> "Raha (RAHA) - Version 2.4.0 🇩🇿"
        AppLanguage.EN -> "Raha (RAHA) - v2.4.0 🇩🇿"
    }

    // --- AMENITIES & BADGES ---
    fun accessiblePmr(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مهيأ لذوي الاحتياجات الخاصة"
        AppLanguage.FR -> "Accessible PMR"
        AppLanguage.EN -> "Wheelchair Accessible"
    }

    fun babyChanging(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "طاولة تغيير حفاضات الأطفال"
        AppLanguage.FR -> "Table à langer pour bébés"
        AppLanguage.EN -> "Baby Changing Table"
    }

    fun womenSection(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "قسم مخصص للنساء"
        AppLanguage.FR -> "Espace réservé aux femmes"
        AppLanguage.EN -> "Women's Section"
    }

    fun hotShower(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دوش واستحمام بالماء الساخن"
        AppLanguage.FR -> "Douche avec eau chaude"
        AppLanguage.EN -> "Hot Shower"
    }

    fun highCleanliness(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مستوى نظافة ممتاز"
        AppLanguage.FR -> "Excellente propreté"
        AppLanguage.EN -> "High Cleanliness"
    }

    fun entryFee(price: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "سعر الدخول: $price دج"
        AppLanguage.FR -> "Tarif d'entrée : $price DZD"
        AppLanguage.EN -> "Entry fee: $price DZD"
    }

    fun freeEntry(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دخول مجاني"
        AppLanguage.FR -> "Accès gratuit"
        AppLanguage.EN -> "Free Access"
    }

    fun coordinatesLabel(coords: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الإحداثيات: $coords"
        AppLanguage.FR -> "Coordonnées : $coords"
        AppLanguage.EN -> "Coordinates: $coords"
    }

    fun savedLocallyRoom(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "💾 مخزن محلياً (Room)"
        AppLanguage.FR -> "💾 Enregistré localement (Room)"
        AppLanguage.EN -> "💾 Stored Locally (Room)"
    }

    fun waterCutAlertTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تنبيه انقطاع المياه في هذا الموقع"
        AppLanguage.FR -> "Alerte : Coupure d'eau signalée"
        AppLanguage.EN -> "Alert: Water cut reported"
    }

    fun waterCutAlertSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "أبلغ مستخدمون عن عدم توفر الماء مؤقتاً. يرجى أخذ الحيطة أو التوجه لأقرب مسجد مجاور."
        AppLanguage.FR -> "Des usagers ont signalé un manque d'eau temporaire. Veuillez vous diriger vers une mosquée voisine."
        AppLanguage.EN -> "Users reported a temporary water cut. Please use a nearby mosque facility."
    }

    fun availableAmenitiesLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الخدمات والمرافق المتوفرة:"
        AppLanguage.FR -> "Services et équipements disponibles :"
        AppLanguage.EN -> "Available Amenities & Features:"
    }

    fun additionalInfo(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "معلومات إضافية:"
        AppLanguage.FR -> "Informations complémentaires :"
        AppLanguage.EN -> "Additional Information:"
    }

    fun donationCcpLabelHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الحساب الجاري للتبرع وصيانة المرفق:"
        AppLanguage.FR -> "Compte CCP pour l'entretien et dons :"
        AppLanguage.EN -> "CCP Account for facility maintenance:"
    }

    fun copyCcpSuccess(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تم نسخ رقم الحساب للتبرع"
        AppLanguage.FR -> "Numéro de compte copié"
        AppLanguage.EN -> "Account number copied"
    }

    fun copyCcpContentDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "نسخ رقم الحساب"
        AppLanguage.FR -> "Copier le numéro de compte"
        AppLanguage.EN -> "Copy account number"
    }

    // --- DONATIONS SCREEN ---
    fun whySupportAppTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "لماذا يحتاج تطبيق «راحة» لدعمكم؟"
        AppLanguage.FR -> "Pourquoi soutenir l'application Raha ?"
        AppLanguage.EN -> "Why support the Raha app?"
    }

    fun whySupportAppBody(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تطبيق «راحة» مبادرة جزائرية مجانية 100% بدون إعلانات مزعجة. تبرعكم الرمزي يساهم مباشرة في تغطية تكاليف الخوادم السحابية، التحقق الميداني من دورات المياه، وتطوير الخرائط التفاعلية بدون إنترنت لخدمة جميع المسافرين."
        AppLanguage.FR -> "L'application Raha est une initiative algérienne 100% gratuite et sans publicités. Votre soutien contribue aux serveurs cloud, à la vérification sur le terrain et au développement de cartes hors-ligne pour tous."
        AppLanguage.EN -> "Raha is a 100% free Algerian initiative with zero ads. Your donation directly funds cloud servers, field verifications, and offline maps development for travelers."
    }

    fun chooseAmountTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "اختر مبلغ المساهمة (دج):"
        AppLanguage.FR -> "Choisissez un montant de contribution (DZD) :"
        AppLanguage.EN -> "Choose donation amount (DZD):"
    }

    fun otherAmountPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مبلغ آخر (دج)"
        AppLanguage.FR -> "Autre montant (DZD)"
        AppLanguage.EN -> "Other amount (DZD)"
    }

    fun paymentMethodTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "طريقة الدفع والتبرع:"
        AppLanguage.FR -> "Mode de paiement :"
        AppLanguage.EN -> "Payment method:"
    }

    fun baridiMobTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تطبيق بريدي موب (BaridiMob)"
        AppLanguage.FR -> "Application BaridiMob"
        AppLanguage.EN -> "BaridiMob App"
    }

    fun baridiMobSub(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تحويل فوري عبر رقم RIP الجزائري"
        AppLanguage.FR -> "Virement instantané via RIP algérien"
        AppLanguage.EN -> "Instant transfer via Algerian RIP"
    }

    fun ccpTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "حوالة بريدية CCP"
        AppLanguage.FR -> "Virement postal CCP"
        AppLanguage.EN -> "CCP Postal Transfer"
    }

    fun ccpSub(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "عبر أي مكتب بريد بريد الجزائر"
        AppLanguage.FR -> "Via tout bureau d'Algérie Poste"
        AppLanguage.EN -> "Via any Algérie Poste office"
    }

    fun flexyTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "فليكسي (تعبئة رصيد)"
        AppLanguage.FR -> "Flexy (Recharge mobile)"
        AppLanguage.EN -> "Flexy (Mobile Top-up)"
    }

    fun flexySub(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "موبيليس / جيزي / أوريدو"
        AppLanguage.FR -> "Mobilis / Djezzy / Ooredoo"
        AppLanguage.EN -> "Mobilis / Djezzy / Ooredoo"
    }

    fun officialRipTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "رقم الحساب البريدي الجاري الموحد (RIP):"
        AppLanguage.FR -> "Relevé d'Identité Postale officiel (RIP) :"
        AppLanguage.EN -> "Official Postal Account (RIP):"
    }

    fun ripHolder(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الحساب: مبادرة راحة لتطوير المرافق العامة"
        AppLanguage.FR -> "Titulaire : Initiative Raha pour les sanitaires"
        AppLanguage.EN -> "Beneficiary: Raha Public Facilities Initiative"
    }

    fun donateNowButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تأكيد وإتمام التبرع 💙"
        AppLanguage.FR -> "Confirmer et finaliser le don 💙"
        AppLanguage.EN -> "Confirm and complete donation 💙"
    }

    fun donationSuccessTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "بارك الله فيك وتقبل منك! 🤲"
        AppLanguage.FR -> "Merci pour votre générosité ! 🤲"
        AppLanguage.EN -> "Thank you for your generous support! 🤲"
    }

    fun donationSuccessMessage(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تم تسجيل رغبتك في الدعم بنجاح. مساهمتك الكريمة تُمكّننا من مواصلة صيانة الخوادم والخرائط وخدمة ضيوف الرحمن والمسافرين في الجزائر."
        AppLanguage.FR -> "Votre intention de don a été enregistrée. Votre soutien permet de maintenir les serveurs, la cartographie et d'aider les voyageurs en Algérie."
        AppLanguage.EN -> "Your contribution intention has been recorded. Your support enables maintaining servers, mapping, and helping travelers across Algeria."
    }

    fun mosqueProjectsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مشاريع صيانة دورات ومصليات المساجد"
        AppLanguage.FR -> "Projets d'entretien des sanitaires des mosquées"
        AppLanguage.EN -> "Mosque Sanitary Maintenance Projects"
    }

    fun mosqueProjectsSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مشاريع معتمدة وموثقة لخدمة بيوت الله وتوفير بيئة نظيفة ومريحة للمصلين وعابري السبيل"
        AppLanguage.FR -> "Projets vérifiés pour l'entretien des lieux de culte et le confort des fidèles"
        AppLanguage.EN -> "Verified projects maintaining ablution areas and comfort for worshippers"
    }

    fun collectedFromTarget(collected: Int, target: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تم جمع $collected من $target دج"
        AppLanguage.FR -> "Collecté : $collected sur $target DZD"
        AppLanguage.EN -> "Collected: $collected of $target DZD"
    }

    fun remainingAmount(amount: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "المتبقي: $amount دج"
        AppLanguage.FR -> "Reste : $amount DZD"
        AppLanguage.EN -> "Remaining: $amount DZD"
    }

    fun donateToMosqueButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التبرع لهذا المسجد مباشرة"
        AppLanguage.FR -> "Faire un don direct à cette mosquée"
        AppLanguage.EN -> "Donate directly to this mosque"
    }

    fun associationLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الجهة المشرفة:"
        AppLanguage.FR -> "Organisme responsable :"
        AppLanguage.EN -> "Supervising Organization:"
    }

    fun projectNeedLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "احتياج المشروع:"
        AppLanguage.FR -> "Besoin du projet :"
        AppLanguage.EN -> "Project Need:"
    }

    fun copyRipButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "نسخ الـ RIP"
        AppLanguage.FR -> "Copier le RIP"
        AppLanguage.EN -> "Copy RIP"
    }

    fun copyCcpButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "نسخ الـ CCP"
        AppLanguage.FR -> "Copier le CCP"
        AppLanguage.EN -> "Copy CCP"
    }

    // --- USER PROFILE STRINGS ---
    fun biometricIdVerified(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "هوية بيومترية موثقة 🇩🇿"
        AppLanguage.FR -> "Identité biométrique vérifiée 🇩🇿"
        AppLanguage.EN -> "Biometric ID Verified 🇩🇿"
    }

    fun biometricIdUnverified(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "هوية غير مسحوبة"
        AppLanguage.FR -> "Identité non scannée"
        AppLanguage.EN -> "ID not scanned"
    }

    fun statReputation(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "نقاط السمعة"
        AppLanguage.FR -> "Points de réputation"
        AppLanguage.EN -> "Reputation Score"
    }

    fun tabIdentity(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "بيانات الهوية"
        AppLanguage.FR -> "Identité"
        AppLanguage.EN -> "Identity"
    }

    fun tabHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "سجل النشاط"
        AppLanguage.FR -> "Historique"
        AppLanguage.EN -> "Activity"
    }

    fun ninField(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "رقم التعريف الوطني (18 رقماً)"
        AppLanguage.FR -> "Numéro d'Identification Nationale (NIN)"
        AppLanguage.EN -> "National ID Number (18 digits)"
    }

    fun dobField(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تاريخ الميلاد"
        AppLanguage.FR -> "Date de naissance"
        AppLanguage.EN -> "Date of Birth"
    }

    fun bloodGroupField(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "فصيلة الدم"
        AppLanguage.FR -> "Groupe sanguin"
        AppLanguage.EN -> "Blood Group"
    }

    fun saveChanges(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "حفظ التعديلات"
        AppLanguage.FR -> "Enregistrer les modifications"
        AppLanguage.EN -> "Save Changes"
    }

    fun scanNationalId(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مسح بطاقة التعريف البيومترية 📷"
        AppLanguage.FR -> "Scanner la carte biométrique 📷"
        AppLanguage.EN -> "Scan Biometric ID Card 📷"
    }

    fun linkedAccounts(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الحسابات المرتبطة"
        AppLanguage.FR -> "Comptes associés"
        AppLanguage.EN -> "Linked Accounts"
    }

    fun googleAccountLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "حساب Google"
        AppLanguage.FR -> "Compte Google"
        AppLanguage.EN -> "Google Account"
    }

    fun linkedStatus(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مرتبط وموثق"
        AppLanguage.FR -> "Lié et sécurisé"
        AppLanguage.EN -> "Linked & Secure"
    }

    fun signOutTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تسجيل الخروج من الحساب"
        AppLanguage.FR -> "Déconnexion du compte"
        AppLanguage.EN -> "Sign out of account"
    }

    fun confirmSignOutPrompt(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "هل أنت متأكد من رغبتك في تسجيل الخروج من التطبيق؟"
        AppLanguage.FR -> "Êtes-vous sûr de vouloir vous déconnecter ?"
        AppLanguage.EN -> "Are you sure you want to sign out?"
    }

    fun noUserContributions(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "لم تقم بإضافة أي دورة مياه بعد"
        AppLanguage.FR -> "Vous n'avez ajouté aucun sanitaire pour le moment"
        AppLanguage.EN -> "You have not added any restrooms yet"
    }

    fun noUserContributionsSub(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ساهم بإضافة دورة مياه أو مرفق وضوء جديد لمساعدة المسافرين."
        AppLanguage.FR -> "Ajoutez un sanitaire ou un lieu d'ablution pour aider les voyageurs."
        AppLanguage.EN -> "Add a new restroom or ablution facility to help fellow travelers."
    }

    fun addPlaceNow(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إضافة الآن"
        AppLanguage.FR -> "Ajouter maintenant"
        AppLanguage.EN -> "Add Now"
    }

    fun scannerTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مسح بطاقة التعريف الوطنية البيومترية"
        AppLanguage.FR -> "Numérisation de la Carte d'Identité Biométrique"
        AppLanguage.EN -> "Biometric National ID Scanning"
    }

    fun scannerInstruction(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ضع البطاقة أمام الكاميرا لمسح الشريط المقروء آلياً (MRZ)"
        AppLanguage.FR -> "Placez la carte face caméra pour lire la bande MRZ"
        AppLanguage.EN -> "Place ID card in front of camera to scan the MRZ code"
    }

    fun captureAndExtract(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "التقاط واستخراج البيانات"
        AppLanguage.FR -> "Capturer et extraire"
        AppLanguage.EN -> "Capture & Extract Data"
    }

    fun confirmExtractedData(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تأكيد واعتماد البيانات"
        AppLanguage.FR -> "Confirmer et valider"
        AppLanguage.EN -> "Confirm & Validate"
    }

    // --- MAP FILTER SHEET ---
    fun filterSheetTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تصفية المعالم على الخريطة"
        AppLanguage.FR -> "Filtrer les lieux sur la carte"
        AppLanguage.EN -> "Filter map locations"
    }

    fun matchingLocationsCount(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "$count نقطة راحة مطابقة للاختيارات"
        AppLanguage.FR -> "$count lieux correspondent à vos critères"
        AppLanguage.EN -> "$count locations match your criteria"
    }

    fun resetFilters(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إعادة ضبط"
        AppLanguage.FR -> "Réinitialiser"
        AppLanguage.EN -> "Reset"
    }

    fun allFilters(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تصفية"
        AppLanguage.FR -> "Filtres"
        AppLanguage.EN -> "Filters"
    }

    fun categoriesHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "أصناف المنشآت:"
        AppLanguage.FR -> "Catégories d'établissements :"
        AppLanguage.EN -> "Facility Categories:"
    }

    fun featuresHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "الخدمات والميزات المطلوبة:"
        AppLanguage.FR -> "Services et fonctionnalités :"
        AppLanguage.EN -> "Features & Services:"
    }

    // --- OFFLINE CACHE DIALOG ---
    fun offlineDialogTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إدارة التخزين المحلي بدون إنترنت (Room)"
        AppLanguage.FR -> "Stockage local hors-ligne (Room)"
        AppLanguage.EN -> "Offline Local Storage (Room)"
    }

    fun offlineDialogSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "قاعدة بيانات محلية سريعة لحفظ وتصفح دورات المياه عبر 69 ولاية في وضع السفر وبدون شبكة."
        AppLanguage.FR -> "Base locale SQLite pour consulter les sanitaires sans connexion à travers 69 wilayas."
        AppLanguage.EN -> "Fast local SQLite database for browsing restrooms across all 69 wilayas without internet."
    }

    fun cachedCountLabel(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "$count موقع مخزن محلياً"
        AppLanguage.FR -> "$count lieux sauvegardés localement"
        AppLanguage.EN -> "$count locations stored locally"
    }

    fun offlineModeSwitchLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تفعيل وضع السفر بدون إنترنت (Offline)"
        AppLanguage.FR -> "Activer le mode hors-ligne (Voyage)"
        AppLanguage.EN -> "Enable Offline Travel Mode"
    }

    fun refreshCacheButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تحديث ومزامنة البيانات المحلية"
        AppLanguage.FR -> "Mettre à jour la base locale"
        AppLanguage.EN -> "Refresh & Sync Local Database"
    }

    fun syncStatusUpToDate(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "قاعدة البيانات محدثة ومتزامنة"
        AppLanguage.FR -> "Base de données à jour et synchronisée"
        AppLanguage.EN -> "Database is up to date and synced"
    }

    // --- PROJECT STUDY SHEET ---
    fun studySheetTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دراسة المشروع والهوية المتكاملة"
        AppLanguage.FR -> "Étude du projet & Identité"
        AppLanguage.EN -> "Project Study & Architecture"
    }

    fun studySheetSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "النموذج الاقتصادي والاستدامة بالجزائر"
        AppLanguage.FR -> "Modèle économique et impact en Algérie"
        AppLanguage.EN -> "Economic model & sustainability in Algeria"
    }

    // --- SIGN IN & AUTH ---
    fun signInWelcomeTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مرحباً بك في تطبيق راحة 🇩🇿"
        AppLanguage.FR -> "Bienvenue sur l'application Raha 🇩🇿"
        AppLanguage.EN -> "Welcome to Raha App 🇩🇿"
    }

    fun signInWelcomeSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دليلك الوطني لدورات المياه ومرافق الوضوء بالجزائر"
        AppLanguage.FR -> "Votre guide national des sanitaires et ablutions en Algérie"
        AppLanguage.EN -> "Your national guide to restrooms and ablution facilities in Algeria"
    }

    fun googleSignInButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تسجيل الدخول بواسطة Google"
        AppLanguage.FR -> "Se connecter avec Google"
        AppLanguage.EN -> "Sign in with Google"
    }

    fun checkingAccount(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "جاري التحقق من الحساب..."
        AppLanguage.FR -> "Vérification du compte..."
        AppLanguage.EN -> "Checking account..."
    }

    fun signInFeatureSync(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مزامنة المفضلة والتقييمات سحابياً عبر جميع أجهزتك"
        AppLanguage.FR -> "Synchronisation cloud de vos favoris et avis"
        AppLanguage.EN -> "Cloud sync for your favorites and reviews across devices"
    }

    fun signInFeatureContribute(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إضافة أماكن جديدة ومساعدة المسافرين في 69 ولاية"
        AppLanguage.FR -> "Ajoutez de nouveaux lieux et aidez les voyageurs dans 69 wilayas"
        AppLanguage.EN -> "Add new places and help travelers in all 69 provinces"
    }

    fun signInFeatureOffline(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "دعم كامل للعمل بدون إنترنت على الطرقات السريعة"
        AppLanguage.FR -> "Support complet hors-ligne sur autoroutes"
        AppLanguage.EN -> "Full offline support on highways"
    }

    fun addRestroom(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "إضافة مرفق"
        AppLanguage.FR -> "Ajouter un lieu"
        AppLanguage.EN -> "Add Restroom"
    }

    fun signOutConfirmTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تأكيد تسجيل الخروج"
        AppLanguage.FR -> "Confirmer la déconnexion"
        AppLanguage.EN -> "Confirm Sign Out"
    }

    fun signOutConfirmMessage(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "هل أنت متأكد من رغبتك في تسجيل الخروج من تطبيق «راحة»؟ يمكنك تسجيل الدخول مجدداً في أي وقت."
        AppLanguage.FR -> "Êtes-vous sûr de vouloir vous déconnecter de l'application « Raha » ? Vous pourrez vous reconnecter à tout moment."
        AppLanguage.EN -> "Are you sure you want to sign out of 'Raha'? You can sign back in at any time."
    }

    fun confirmSignOut(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "نعم، تسجيل الخروج"
        AppLanguage.FR -> "Oui, se déconnecter"
        AppLanguage.EN -> "Yes, Sign Out"
    }

    fun connectedBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "متصل ✓"
        AppLanguage.FR -> "Connecté ✓"
        AppLanguage.EN -> "Connected ✓"
    }

    fun linkAction(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "ربط"
        AppLanguage.FR -> "Lier"
        AppLanguage.EN -> "Link"
    }

    fun freeLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "مجاني"
        AppLanguage.FR -> "Gratuit"
        AppLanguage.EN -> "Free"
    }

    fun reviewsCountSuffix(lang: AppLanguage): String = when (lang) {
        AppLanguage.AR -> "تقييم"
        AppLanguage.FR -> "avis"
        AppLanguage.EN -> "reviews"
    }
}

