package com.example.anhdt.smartalarm.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anhdt.smartalarm.R;

public class DisPlayWebPageActivity extends BaseActivity {
	
	WebView webview;
	ImageView btn_back;
    TextView title_Page;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
		Intent in = getIntent();
		String page_url = in.getStringExtra("page_url");
        String title = in.getStringExtra("Title");
		btn_back = (ImageView) findViewById(R.id.btn_back);
        title_Page = (TextView) findViewById(R.id.title_Page);
		webview = (WebView) findViewById(R.id.webpage);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl(page_url);
		title_Page.setText(title);
		webview.setWebViewClient(new DisPlayWebPageActivityClient());
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
	    	webview.goBack();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private class DisPlayWebPageActivityClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	}

}


