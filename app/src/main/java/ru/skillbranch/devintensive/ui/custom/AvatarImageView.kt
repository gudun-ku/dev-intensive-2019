package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.textdrawable.TextDrawable
import ru.skillbranch.devintensive.utils.Utils


open class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0

): CircleImageView (context, attrs, defStyleAttr) {

    var userDrawable: Drawable? = null

    fun setInitials(initials: String) {

        userDrawable = makeInitialsDrawable(initials)
        if (userDrawable != null) {
            super.loadImageDrawable(userDrawable)
        }

    }

    //TODO - here need to make measurements
    private fun makeInitialsDrawable(initials: String): Drawable? {

        return TextDrawable
            .builder()
            .beginConfig()
            .width(App.applicationContext().resources.getDimension(R.dimen.btn_round_size_40).toInt())
            .height(App.applicationContext().resources.getDimension(R.dimen.btn_round_size_40).toInt())
            .fontSize(Utils.convertSpToPx(20f))
            .endConfig()
            .buildRound(initials, Color.WHITE)
    }
}