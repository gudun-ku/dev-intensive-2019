package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils


open class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0

): CircleImageView (context, attrs, defStyleAttr) {


    fun setInitials(initials: String) {
        if (initials.isNullOrBlank()) {
            setImageDrawable(makeInitialsDrawable("??"))
        } else {
            setImageDrawable(makeInitialsDrawable(initials))
        }
    }

    private fun makeInitialsDrawable(initials: String): Drawable? {

        return Utils.TextDrawable
            .builder()
            .beginConfig()
            .width(App.applicationContext().resources.getDimension(R.dimen.btn_round_size_40).toInt())
            .height(App.applicationContext().resources.getDimension(R.dimen.btn_round_size_40).toInt())
            .fontSize(Utils.convertSpToPx(16f))
            .endConfig()
            .buildRound(initials ,Utils.ColorHash.getColor(initials))
    }

}