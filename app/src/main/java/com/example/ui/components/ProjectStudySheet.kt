package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PersianGreen
import com.example.ui.theme.RahaBlue
import com.example.ui.theme.RahaGreen
import com.example.ui.theme.RahaGreenContainer
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.WaterBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectStudySheet(
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.img_raha_logo),
                        contentDescription = "شعار راحة",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "تطبيق راحة",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = RahaBlue
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = RahaGreenContainer
                            ) {
                                Text(
                                    text = "RAHA",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = RahaGreen
                                    ),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "دراسة المشروع والهوية البصرية المتكاملة",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Brand Identity Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = RahaGreenContainer.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_raha_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "الهوية البصرية: راحة (RAHA)",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = RahaBlue
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "اللون الأخضر الزمردي يرمز للنظافة والحيوية ورمز الموقع، والأزرق الكوبالتي يرمز للثقة وراحة البال.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 1: Executive Summary
            StudySectionCard(
                title = "1. ملخص الفكرة والقيمة المضافة (Value Proposition)",
                icon = Icons.Default.Lightbulb,
                color = TealPrimary
            ) {
                Text(
                    text = "يعاني المواطنون والمسافرون والسياح في الجزائر من صعوبة بالغة في العثور على مراحيض عمومية نظيفة وسريعة الوصول عند التنقل. التطبيق يحل هذه المشكلة بجمع كافة الخيارات المتاحة على خريطة تفاعلية ذكية تعمل حتى مع اتصال إنترنت ضعيف، مع ترتيب الأقرب حسب المسافة وتوضيح حالة النظافة وتوفر الماء وقسم مخصص للنساء.",
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section 2: Reality & Alternatives in Algeria
            StudySectionCard(
                title = "2. خارطة المرافق المتاحة في الواقع الجزائري",
                icon = Icons.Default.Business,
                color = WaterBlue
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    BulletPoint("المساجد (المصدر الأول والأكثر انتشاراً): أكثر من 20 ألف مسجد في الجزائر توفر مرافق وضوء ودورات مياه مجانية ومفتوحة خلال أوقات الصلوات، وهي الملاذ الأول للمواطنين.")
                    BulletPoint("محطات نفطال على الطرق السريعة: نقاط ارتكاز حيوية على الطريق السيار شرق-غرب والطريق العابر للصحراء (RN1)، وتتميز بالفتح 24/7.")
                    BulletPoint("محطات النقل العمومي: محطات حافلات سوجرال (SOGRAL)، محطات الميترو والترامواي، ومحطات قطارات SNTF.")
                    BulletPoint("المراكز التجارية والمولات: مثل باب الزوار، جاردن سيتي، إيسينيا مول، بارك مول سطيف (نظافة ممتازة وتوفر دائم).")
                    BulletPoint("الحمامات الشعبية التقليدية: مؤسسات متجذرة في الثقافة الجزائرية توفر دوش واستحمام بأسعار رمزية (150-250 دج).")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section 3: Key Challenges
            StudySectionCard(
                title = "3. التحديات الخاصة بالسياق الجزائري وكيفية حلها",
                icon = Icons.Default.Warning,
                color = Color(0xFFE76F51)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    BulletPoint("تذبذب تزويد المياه: حل عبر زر تفاعلي مجتمعي (إبلاغ عن انقطاع الماء) لتنبيه المستخدمين آنياً.")
                    BulletPoint("الحفاظ على خصوصية النساء: فرز دقيق للأماكن التي تحتوي على جناح نسائي منفصل ومغلق.")
                    BulletPoint("جودة وتحديث البيانات: الاعتماد على مبدأ التعهيد الجماعي (Crowdsourcing) بمكافأة المستخدمين النشطين بنقاط وشارات موثوقية.")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section 4: Monetization Strategy
            StudySectionCard(
                title = "4. نموذج العمل وتوليد الإيرادات (Business Model)",
                icon = Icons.Default.MonetizationOn,
                color = PersianGreen
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    BulletPoint("شراكات مع الحمامات الخاصة ومحطات الوقود للظهور كأماكن موصى بها (Featured / Verified).")
                    BulletPoint("إعلانات محلية موجهة للشركات الناشطة في منتجات النظافة الشخصية والمياه المعدنية.")
                    BulletPoint("بيانات مجهولة الهوية لخدمة البلديات والمخططين الحضريين حول أماكن الطلب المرتفع لإنشاء مرافق جديدة.")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section 5: Technical Stack Recommendations
            StudySectionCard(
                title = "5. التوصيات التقنية لتطوير التطبيق",
                icon = Icons.Default.CheckCircle,
                color = TealPrimary
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    BulletPoint("تخزين محلي Offline-First عبر Room Database ليعمل التطبيق حتى في الأنفاق ومحطات السفر التي ينقطع فيها الإنترنت.")
                    BulletPoint("الاستفادة من بيانات OpenStreetMap (OSM) عبر Overpass API كبيانات تأسيسية أولية.")
                    BulletPoint("تكامل فوري وسلس مع خرائط جوجل بنقرة واحدة للتوجيه والملاحة (Turn-by-turn Navigation).")
                }
            }
        }
    }
}

@Composable
fun StudySectionCard(
    title: String,
    icon: ImageVector,
    color: Color,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
fun BulletPoint(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = TealPrimary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp
        )
    }
}
