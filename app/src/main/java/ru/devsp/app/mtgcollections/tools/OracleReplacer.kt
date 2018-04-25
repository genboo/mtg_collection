package ru.devsp.app.mtgcollections.tools

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.DisplayMetrics


object OracleReplacer {
    private fun replace(text: String): String =
            if (text.isNotEmpty()) {
                text
                        .replace("\n", "<br/>")
                        .replace("{B}", "<img src=\"ic_b\"/>")
                        .replace("{U}", "<img src=\"ic_u\"/>")
                        .replace("{W}", "<img src=\"ic_w\"/>")
                        .replace("{R}", "<img src=\"ic_r\"/>")
                        .replace("{G}", "<img src=\"ic_g\"/>")
                        .replace("{C}", "<img src=\"ic_c\"/>")
                        .replace("{R/W}", "<img src=\"ic_rw\"/>")
                        .replace("{W/U}", "<img src=\"ic_wu\"/>")
                        .replace("{W/B}", "<img src=\"ic_wb\"/>")
                        .replace("{U/B}", "<img src=\"ic_ub\"/>")
                        .replace("{U/R}", "<img src=\"ic_ur\"/>")
                        .replace("{B/R}", "<img src=\"ic_br\"/>")
                        .replace("{B/G}", "<img src=\"ic_bg\"/>")
                        .replace("{R/G}", "<img src=\"ic_rg\"/>")
                        .replace("{G/W}", "<img src=\"ic_gw\"/>")
                        .replace("{G/U}", "<img src=\"ic_gu\"/>")
                        .replace("{B/P}", "<img src=\"ic_bp\"/>")
                        .replace("{G/P}", "<img src=\"ic_gp\"/>")
                        .replace("{U/P}", "<img src=\"ic_up\"/>")
                        .replace("{W/P}", "<img src=\"ic_wp\"/>")
                        .replace("{R/P}", "<img src=\"ic_rp\"/>")
                        .replace("{T}", "<img src=\"ic_tap\"/>")
                        .replace("{E}", "<img src=\"ic_e\"/>")
                        .replace("{0}", "<img src=\"ic_0\"/>")
                        .replace("{1}", "<img src=\"ic_1\"/>")
                        .replace("{2}", "<img src=\"ic_2\"/>")
                        .replace("{3}", "<img src=\"ic_3\"/>")
                        .replace("{4}", "<img src=\"ic_4\"/>")
                        .replace("{5}", "<img src=\"ic_5\"/>")
                        .replace("{6}", "<img src=\"ic_6\"/>")
                        .replace("{7}", "<img src=\"ic_7\"/>")
                        .replace("{8}", "<img src=\"ic_8\"/>")
                        .replace("{9}", "<img src=\"ic_9\"/>")
                        .replace("{10}", "<img src=\"ic_10\"/>")
                        .replace("{11}", "<img src=\"ic_11\"/>")
                        .replace("{12}", "<img src=\"ic_12\"/>")
                        .replace("{13}", "<img src=\"ic_13\"/>")
                        .replace("{14}", "<img src=\"ic_14\"/>")
                        .replace("{14}", "<img src=\"ic_14\"/>")
                        .replace("{15}", "<img src=\"ic_15\"/>")
                        .replace("{16}", "<img src=\"ic_16\"/>")
                        .replace("{17}", "<img src=\"ic_17\"/>")
                        .replace("{18}", "<img src=\"ic_18\"/>")
                        .replace("{19}", "<img src=\"ic_19\"/>")
                        .replace("{20}", "<img src=\"ic_20\"/>")
                        .replace("{X}", "<img src=\"ic_x\"/>")
                        .replace("{Y}", "<img src=\"ic_y\"/>")
                        .replace("{Z}", "<img src=\"ic_z\"/>")

            } else {
                ""
            }

    @Suppress("deprecation")
    @TargetApi(Build.VERSION_CODES.N)
    fun getText(text: String, activity: Activity): Spanned = when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        true -> Html.fromHtml(replace(text), Html.FROM_HTML_MODE_LEGACY, Html.ImageGetter { getDrawable(it, activity) }, null)
        else -> Html.fromHtml(replace(text), Html.ImageGetter { getDrawable(it, activity) }, null)
    }

    private fun getDrawable(source: String, activity: Activity): Drawable {
        val drawable = activity.resources.getDrawable(activity.resources.getIdentifier(source, "drawable", activity.packageName), activity.theme)
        val metrics = activity.resources.displayMetrics
        val px = 16 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)

        drawable.setBounds(0, 0, px, px)
        return drawable
    }
}