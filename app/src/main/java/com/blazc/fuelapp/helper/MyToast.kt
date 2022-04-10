package com.blazc.fuelapp.helper

import android.app.Activity
import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.blazc.fuelapp.R
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class MyToast {
    companion object {
        fun showSuccess(context: Context, title: String, content: String) {
            MotionToast.createToast(
                context as Activity,
                title,
                content,
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(context, R.font.poppins))
        }

        fun showError(context: Context, title: String, content: String) {
            MotionToast.createToast(
                context as Activity,
                title,
                content,
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(context, R.font.poppins))
        }

        fun showInfo(context: Context, title: String, content: String) {
            MotionToast.createToast(
                context as Activity,
                title,
                content,
                MotionToastStyle.INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(context, R.font.poppins))
        }

        fun showWarning(context: Context, title: String, content: String) {
            MotionToast.createToast(
                context as Activity,
                title,
                content,
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(context, R.font.poppins))
        }
    }
}