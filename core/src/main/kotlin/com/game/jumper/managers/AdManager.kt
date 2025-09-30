package com.game.jumper.managers

/**
 * Interface for AdMob integration
 * Android module will implement this interface with actual AdMob functionality
 */
interface AdManager {
    /**
     * Show an interstitial ad (e.g., on game over)
     */
    fun showInterstitialAd()

    /**
     * Show a rewarded ad and call the callback when user completes watching
     * @param onRewarded Callback invoked when user successfully watches the ad
     */
    fun showRewardedAd(onRewarded: () -> Unit)

    /**
     * Check if rewarded ad is loaded and ready to show
     */
    fun isRewardedAdReady(): Boolean

    /**
     * Load a new interstitial ad
     */
    fun loadInterstitialAd()

    /**
     * Load a new rewarded ad
     */
    fun loadRewardedAd()
}