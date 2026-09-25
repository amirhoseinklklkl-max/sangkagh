package com.ancientpersia.rps.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ancientpersia.rps.MainActivity
import com.ancientpersia.rps.R
import com.ancientpersia.rps.audio.SoundManager
import com.ancientpersia.rps.data.BackgroundRepo
import com.ancientpersia.rps.data.GamePrefs
import com.ancientpersia.rps.databinding.FragmentMenuBinding
import com.ancientpersia.rps.util.PersianUtil
import com.ancientpersia.rps.util.Res

/**
 * منوی اصلی: خورشید چرخان + شروع بازی، فروشگاه، خروج + باکس سکه و آمار
 */
class MenuFragment : Fragment() {

    private var _b: FragmentMenuBinding? = null
    private val b get() = _b!!
    private var floatAnim: ObjectAnimator? = null
    private var sunRaysAnim: ObjectAnimator? = null
    private var sunCoreAnim: ObjectAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentMenuBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val act = activity as MainActivity

        b.btnStart.setOnClickListener {
            SoundManager.play("click")
            act.showGame()
        }
        b.btnShop.setOnClickListener {
            SoundManager.play("click")
            act.showShop()
        }
        b.btnExit.setOnClickListener {
            SoundManager.play("click")
            confirmExit(act)
        }

        // ظاهر شدن عنوان با انیمیشن
        b.titleText.scaleX = 0.6f
        b.titleText.scaleY = 0.6f
        b.titleText.alpha = 0f
        b.titleText.animate()
            .scaleX(1f).scaleY(1f).alpha(1f)
            .setDuration(520)
            .setInterpolator(OvershootInterpolator())
            .start()

        // انیمیشن معلق بودن باکس آمار
        floatAnim = ObjectAnimator.ofFloat(b.statsBox, View.TRANSLATION_Y, 0f, -8f, 0f).apply {
            duration = 2300
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            start()
        }

        // خورشید سنتی ایرانی: امواج نور اطراف ساعتگرد، دایره مرکزی پادساعتگرد
        startSun()
    }

    private fun startSun() {
        sunRaysAnim?.cancel()
        sunCoreAnim?.cancel()
        sunRaysAnim = ObjectAnimator.ofFloat(b.sunRays, View.ROTATION, 0f, 360f).apply {
            duration = 14000L
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
        sunCoreAnim = ObjectAnimator.ofFloat(b.sunCore, View.ROTATION, 0f, -360f).apply {
            duration = 22000L
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
        if (_b != null) startSun()
    }

    override fun onPause() {
        sunRaysAnim?.cancel()
        sunCoreAnim?.cancel()
        super.onPause()
    }

    private fun refresh() {
        b.tvCoins.text = PersianUtil.toFa(GamePrefs.coins)
        b.tvWins.text = "پیروزی: ${PersianUtil.toFa(GamePrefs.wins)}"
        b.tvLosses.text = "باخت: ${PersianUtil.toFa(GamePrefs.losses)}"
        b.bgImage.setImageResource(
            Res.drawable(requireContext(), BackgroundRepo.drawableName(GamePrefs.equippedBackground))
        )
    }

    private fun confirmExit(act: MainActivity) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.exit_title)
            .setMessage(R.string.exit_msg)
            .setPositiveButton(R.string.yes_exit) { _, _ -> act.finishAffinity() }
            .setNegativeButton(R.string.no_stay, null)
            .show()
    }

    override fun onDestroyView() {
        floatAnim?.cancel()
        floatAnim = null
        sunRaysAnim?.cancel()
        sunCoreAnim?.cancel()
        sunRaysAnim = null
        sunCoreAnim = null
        _b = null
        super.onDestroyView()
    }
}
