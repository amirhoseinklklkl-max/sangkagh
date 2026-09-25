package com.ancientpersia.rps.data

import android.content.Context
import android.content.SharedPreferences

/**
 * حافظه دائمی بازی: سکه‌ها، آمار برد/باخت، پوست‌ها و پس‌زمینه‌ها
 */
object GamePrefs {

    private const val NAME = "skg_prefs"
    private lateinit var sp: SharedPreferences

    fun init(ctx: Context) {
        if (!::sp.isInitialized) {
            sp = ctx.applicationContext.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        }
    }

    /** موجودی سکه کاربر (هرگز منفی نمی‌شود) */
    var coins: Int
        get() = sp.getInt(KEY_COINS, 100)
        set(value) = sp.edit().putInt(KEY_COINS, value.coerceAtLeast(0)).apply()

    /** تعداد نبردهای برده‌شده */
    var wins: Int
        get() = sp.getInt(KEY_WINS, 0)
        set(value) = sp.edit().putInt(KEY_WINS, value).apply()

    /** تعداد نبردهای باخته‌شده */
    var losses: Int
        get() = sp.getInt(KEY_LOSSES, 0)
        set(value) = sp.edit().putInt(KEY_LOSSES, value).apply()

    // ---------- skins ----------

    fun ownsSkin(id: String): Boolean = ownedSet(KEY_OWNED_SKINS, setOf("default")).contains(id)

    fun addOwnedSkin(id: String) {
        val s = ownedSet(KEY_OWNED_SKINS, setOf("default"))
        s.add(id)
        sp.edit().putStringSet(KEY_OWNED_SKINS, s).apply()
    }

    var equippedSkin: String
        get() = sp.getString(KEY_EQUIPPED_SKIN, "default") ?: "default"
        set(value) = sp.edit().putString(KEY_EQUIPPED_SKIN, value).apply()

    // ---------- backgrounds ----------

    fun ownsBackground(id: String): Boolean =
        ownedSet(KEY_OWNED_BGS, setOf("persepolis")).contains(id)

    fun addOwnedBackground(id: String) {
        val s = ownedSet(KEY_OWNED_BGS, setOf("persepolis"))
        s.add(id)
        sp.edit().putStringSet(KEY_OWNED_BGS, s).apply()
    }

    var equippedBackground: String
        get() = sp.getString(KEY_EQUIPPED_BG, "persepolis") ?: "persepolis"
        set(value) = sp.edit().putString(KEY_EQUIPPED_BG, value).apply()

    // ---------- helpers ----------

    private fun ownedSet(key: String, def: Set<String>): MutableSet<String> =
        HashSet(sp.getStringSet(key, def) ?: def)

    private const val KEY_COINS = "coins"
    private const val KEY_WINS = "wins"
    private const val KEY_LOSSES = "losses"
    private const val KEY_OWNED_SKINS = "owned_skins"
    private const val KEY_EQUIPPED_SKIN = "equipped_skin"
    private const val KEY_OWNED_BGS = "owned_bgs"
    private const val KEY_EQUIPPED_BG = "equipped_bg"
}
