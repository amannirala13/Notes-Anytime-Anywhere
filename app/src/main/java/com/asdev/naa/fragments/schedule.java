package com.asdev.naa.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.asdev.naa.R;

import java.util.Objects;

public class schedule extends Fragment {

    private WebView schedulePage;
    private WebViewClient scheduleClient;
    private LottieAnimationView loadingAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        schedulePage = Objects.requireNonNull(getView()).findViewById(R.id.schedule_page);
        loadingAnimation = getView().findViewById(R.id.loading_anim);

       loadingView();
        schedulePage.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(Uri.parse(url).getHost().endsWith("time-table.sdrclabs.in")) {
                    return false;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                return true;
            }
        });

        schedulePage.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                if(progress>=80)
                    scheduleView();
            }});

        WebSettings scheduleSettings = schedulePage.getSettings();
        scheduleSettings.setJavaScriptEnabled(true);
        scheduleSettings.setAllowContentAccess(true);
      //  scheduleSettings.setBuiltInZoomControls(true);
        scheduleSettings.setSupportMultipleWindows(true);
        scheduleSettings.setDisplayZoomControls(false);
        scheduleSettings.setUseWideViewPort(true);
        scheduleSettings.setBuiltInZoomControls(true);
        scheduleSettings.setAppCacheEnabled(true);
        scheduleSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            scheduleSettings.setSafeBrowsingEnabled(true);
        }
        schedulePage.loadUrl("http://time-table.sdrclabs.in");

    }

    private void loadingView() {
        loadingAnimation.setVisibility(View.VISIBLE);
        schedulePage.setVisibility(View.GONE);
    }

    private void scheduleView()
    {
        loadingAnimation.setVisibility(View.GONE);
        schedulePage.setVisibility(View.VISIBLE);
    }

    public boolean canGoBack()
    {
        if (schedulePage.canGoBack())
            return true;
        else
            return false;
    }

    
   public void goBack()
   {
           schedulePage.goBack();
   }
    

}