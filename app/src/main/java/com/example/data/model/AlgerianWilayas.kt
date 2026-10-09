package com.example.data.model

/**
 * Data model for Algerian Wilayas (58 Wilayas).
 */
data class WilayaInfo(
    val code: Int,
    val arabicName: String,
    val frenchName: String,
    val latitude: Double,
    val longitude: Double
) {
    val displayName: String
        get() = "${String.format("%02d", code)} - $arabicName"

    val fullSearchableText: String
        get() = "$code $arabicName $frenchName".lowercase()
}

/**
 * Official registry of all 58 Algerian Wilayas.
 */
object AlgerianWilayas {
    val all58Wilayas: List<WilayaInfo> = listOf(
        WilayaInfo(1, "أدرار", "Adrar", 27.8742, -0.2939),
        WilayaInfo(2, "الشلف", "Chlef", 36.1653, 1.3345),
        WilayaInfo(3, "الأغواط", "Laghouat", 33.8000, 2.8651),
        WilayaInfo(4, "أم البواقي", "Oum El Bouaghi", 35.8754, 7.1135),
        WilayaInfo(5, "باتنة", "Batna", 35.5560, 6.1741),
        WilayaInfo(6, "بجاية", "Béjaïa", 36.7559, 5.0843),
        WilayaInfo(7, "بسكرة", "Biskra", 34.8504, 5.7281),
        WilayaInfo(8, "بشار", "Béchar", 31.6167, -2.2167),
        WilayaInfo(9, "البليدة", "Blida", 36.4700, 2.8300),
        WilayaInfo(10, "البويرة", "Bouira", 36.3749, 3.9020),
        WilayaInfo(11, "تمنراست", "Tamanrasset", 22.7850, 5.5228),
        WilayaInfo(12, "تبسة", "Tébessa", 35.4042, 8.1242),
        WilayaInfo(13, "تلمسان", "Tlemcen", 34.8783, -1.3150),
        WilayaInfo(14, "تيارت", "Tiaret", 35.3710, 1.3170),
        WilayaInfo(15, "تيزي وزو", "Tizi Ouzou", 36.7118, 4.0459),
        WilayaInfo(16, "الجزائر العاصمة", "Alger", 36.7538, 3.0588),
        WilayaInfo(17, "الجلفة", "Djelfa", 34.6728, 3.2630),
        WilayaInfo(18, "جيجل", "Jijel", 36.8206, 5.7667),
        WilayaInfo(19, "سطيف", "Sétif", 36.1912, 5.4078),
        WilayaInfo(20, "سعيدة", "Saïda", 34.8303, 0.1517),
        WilayaInfo(21, "سكيكدة", "Skikda", 36.8792, 6.9078),
        WilayaInfo(22, "سيدي بلعباس", "Sidi Bel Abbès", 35.1899, -0.6308),
        WilayaInfo(23, "عنابة", "Annaba", 36.8870, 7.7450),
        WilayaInfo(24, "قالمة", "Guelma", 36.4621, 7.4261),
        WilayaInfo(25, "قسنطينة", "Constantine", 36.3498, 6.6192),
        WilayaInfo(26, "المدية", "Médéa", 36.2642, 2.7539),
        WilayaInfo(27, "مستغانم", "Mostaganem", 35.9312, 0.0892),
        WilayaInfo(28, "المسيلة", "M'Sila", 35.7058, 4.5419),
        WilayaInfo(29, "معسكر", "Mascara", 35.3966, 0.1403),
        WilayaInfo(30, "ورقلة", "Ouargla", 31.9530, 5.3300),
        WilayaInfo(31, "وهران", "Oran", 35.6987, -0.6345),
        WilayaInfo(32, "البيض", "El Bayadh", 33.6832, 1.0193),
        WilayaInfo(33, "إليزي", "Illizi", 26.5050, 8.4820),
        WilayaInfo(34, "برج بوعريريج", "Bordj Bou Arreridj", 36.0732, 4.7611),
        WilayaInfo(35, "بومرداس", "Boumerdès", 36.7667, 3.4667),
        WilayaInfo(36, "الطارف", "El Tarf", 36.7672, 8.3138),
        WilayaInfo(37, "تندوف", "Tindouf", 27.6711, -8.1474),
        WilayaInfo(38, "تسمسيلت", "Tissemsilt", 35.6072, 1.8109),
        WilayaInfo(39, "الوادي", "El Oued", 33.3683, 6.8674),
        WilayaInfo(40, "خنشلة", "Khenchela", 35.4358, 7.1433),
        WilayaInfo(41, "سوق أهراس", "Souk Ahras", 36.2864, 7.9511),
        WilayaInfo(42, "تيبازة", "Tipaza", 36.5925, 2.4475),
        WilayaInfo(43, "ميلة", "Mila", 36.4503, 6.2644),
        WilayaInfo(44, "عين الدفلى", "Aïn Defla", 36.2639, 1.9678),
        WilayaInfo(45, "النعامة", "Naâma", 33.2667, -0.3167),
        WilayaInfo(46, "عين تموشنت", "Aïn Témouchent", 35.2975, -1.1404),
        WilayaInfo(47, "غرداية", "Ghardaïa", 32.4909, 3.6735),
        WilayaInfo(48, "غليزان", "Relizane", 35.7373, 0.5558),
        WilayaInfo(49, "تيميمون", "Timimoun", 29.2639, 0.2310),
        WilayaInfo(50, "برج باجي مختار", "Bordj Badji Mokhtar", 21.3278, 0.9208),
        WilayaInfo(51, "أولاد جلال", "Ouled Djellal", 34.4333, 5.0667),
        WilayaInfo(52, "بني عباس", "Béni Abbès", 30.1333, -2.1667),
        WilayaInfo(53, "إن صالح", "In Salah", 27.1935, 2.4608),
        WilayaInfo(54, "إن قزام", "In Guezzam", 19.5694, 5.7708),
        WilayaInfo(55, "تقرت", "Touggourt", 33.1053, 6.0583),
        WilayaInfo(56, "جانت", "Djanet", 24.5539, 9.4847),
        WilayaInfo(57, "المغير", "El M'Ghair", 33.9500, 5.9167),
        WilayaInfo(58, "المنيعة", "El Meniaa", 30.5847, 2.8797)
    )

    val namesList: List<String> = all58Wilayas.map { it.arabicName }

    fun findByName(name: String): WilayaInfo? {
        return all58Wilayas.find { 
            it.arabicName.equals(name, ignoreCase = true) || 
            it.displayName.equals(name, ignoreCase = true) ||
            it.frenchName.equals(name, ignoreCase = true)
        }
    }
}
