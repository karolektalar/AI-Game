package com.game.jumper

import android.app.Activity
import android.util.Log
import com.badlogic.gdx.Gdx
import com.game.jumper.managers.AdManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

/**
 * Android implementation of AdManager using Google AdMob
 * Handles loading and showing of interstitial and rewarded ads
 */
class AndroidAdManager(private val activity: Activity) : AdManager {

    companion object {
        private const val TAG = "AndroidAdManager"

        // TEST AD UNITS - Replace with your real ad unit IDs before release
        private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        private const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
    }

    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private var isInterstitialLoading = false
    private var isRewardedLoading = false
    private var pendingRewardCallback: (() -> Unit)? = null

    init {
        // Initialize Mobile Ads SDK
        activity.runOnUiThread {
            MobileAds.initialize(activity) { initializationStatus ->
                Log.d(TAG, "AdMob initialized: ${initializationStatus.adapterStatusMap}")
            }

            // Load initial ads
            loadInterstitialAd()
            loadRewardedAd()
        }
    }

    override fun showInterstitialAd() {
        activity.runOnUiThread {
            interstitialAd?.let { ad ->
                ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Interstitial ad dismissed")
                        interstitialAd = null
                        loadInterstitialAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(error: AdError) {
                        Log.e(TAG, "Interstitial ad failed to show: ${error.message}")
                        interstitialAd = null
                        loadInterstitialAd()
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Interstitial ad showed")
                    }
                }

                ad.show(activity)
            } ?: run {
                Log.d(TAG, "Interstitial ad not ready")
                loadInterstitialAd()
            }
        }
    }

    override fun showRewardedAd(onRewarded: () -> Unit) {
        pendingRewardCallback = onRewarded

        activity.runOnUiThread {
            rewardedAd?.let { ad ->
                ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Rewarded ad dismissed")
                        rewardedAd = null
                        loadRewardedAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(error: AdError) {
                        Log.e(TAG, "Rewarded ad failed to show: ${error.message}")
                        rewardedAd = null
                        loadRewardedAd()
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Rewarded ad showed")
                    }
                }

                ad.show(activity) { rewardItem ->
                    Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                    // Call reward callback on GL thread
                    Gdx.app.postRunnable {
                        pendingRewardCallback?.invoke()
                        pendingRewardCallback = null
                    }
                }
            } ?: run {
                Log.d(TAG, "Rewarded ad not ready")
                loadRewardedAd()
            }
        }
    }

    override fun isRewardedAdReady(): Boolean {
        return rewardedAd != null
    }

    override fun loadInterstitialAd() {
        if (isInterstitialLoading || interstitialAd != null) {
            return
        }

        activity.runOnUiThread {
            isInterstitialLoading = true

            val adRequest = AdRequest.Builder().build()

            InterstitialAd.load(
                activity,
                INTERSTITIAL_AD_UNIT_ID,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        Log.d(TAG, "Interstitial ad loaded")
                        interstitialAd = ad
                        isInterstitialLoading = false
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e(TAG, "Interstitial ad failed to load: ${error.message}")
                        interstitialAd = null
                        isInterstitialLoading = false
                    }
                }
            )
        }
    }

    override fun loadRewardedAd() {
        if (isRewardedLoading || rewardedAd != null) {
            return
        }

        activity.runOnUiThread {
            isRewardedLoading = true

            val adRequest = AdRequest.Builder().build()

            RewardedAd.load(
                activity,
                REWARDED_AD_UNIT_ID,
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        Log.d(TAG, "Rewarded ad loaded")
                        rewardedAd = ad
                        isRewardedLoading = false
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e(TAG, "Rewarded ad failed to load: ${error.message}")
                        rewardedAd = null
                        isRewardedLoading = false
                    }
                }
            )
        }
    }

    fun resume() {
        // Called when activity resumes
    }

    fun pause() {
        // Called when activity pauses
    }

    fun destroy() {
        // Cleanup
        interstitialAd = null
        rewardedAd = null
        pendingRewardCallback = null
    }
}