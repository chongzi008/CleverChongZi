package com.example.chongzi007.cleverchongzi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.chongzi007.cleverchongzi.R;


/**
 * 新闻详情页
 * 
 * @author Kevin
 * 
 */
public class NewsDetailActivity extends Activity implements OnClickListener{

	private WebView mWebView;
	private ImageButton btnBack;
	private ImageButton btnSize;
	private ImageButton btnShare;

	private ProgressBar pbProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);

		mWebView = (WebView) findViewById(R.id.wv_web);
		btnBack = (ImageButton) findViewById(R.id.btn_back);
		btnSize = (ImageButton) findViewById(R.id.btn_size);
		btnShare = (ImageButton) findViewById(R.id.btn_share);

		btnBack.setOnClickListener(this);
		btnSize.setOnClickListener(this);
		btnShare.setOnClickListener(this);

		pbProgress = (ProgressBar) findViewById(R.id.pb_progress);

		String url = getIntent().getStringExtra("url");

		WebSettings settings = mWebView.getSettings();
			settings.setJavaScriptEnabled(true);// 表示支持js
			settings.setBuiltInZoomControls(true);// 显示放大缩小按钮
			settings.setUseWideViewPort(true);// 支持双击缩放

			mWebView.setWebViewClient(new WebViewClient() {

			/**
			 * 网页开始加载
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out.println("网页开始加载");
				pbProgress.setVisibility(View.VISIBLE);
			}

			/**
			 * 网页加载结束
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("网页开始结束");

				pbProgress.setVisibility(View.GONE);
			}

			/**
			 * 所有跳转的链接都会在此方法中回调
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// tel:110
				System.out.println("跳转url:" + url);
				view.loadUrl(url);

				return true;
				// return super.shouldOverrideUrlLoading(view, url);
			}
		});


		mWebView.loadUrl(url);// 加载网页
	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_size:
				showChooseDialog();
				break;
			case R.id.btn_share:
				//showShare();
				break;

			default:
				break;
		}
	}

	private int mCurrentChooseItem;// 记录当前选中的item, 点击确定前
	private int mCurrentItem = 2;// 记录当前选中的item, 点击确定后
	/**
	 * 显示选择对话框
	 */
	private void showChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
				"超小号字体" };
		builder.setTitle("字体设置");
		builder.setSingleChoiceItems(items, mCurrentItem,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						mCurrentChooseItem = which;
					}
				});

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				WebSettings settings = mWebView.getSettings();
				switch (mCurrentChooseItem) {
					case 0:
						settings.setTextSize(TextSize.LARGEST);
						break;
					case 1:
						settings.setTextSize(TextSize.LARGER);
						break;
					case 2:
						settings.setTextSize(TextSize.NORMAL);
						break;
					case 3:
						settings.setTextSize(TextSize.SMALLER);
						break;
					case 4:
						settings.setTextSize(TextSize.SMALLEST);
						break;

					default:
						break;
				}

				mCurrentItem = mCurrentChooseItem;
			}
		});

		builder.setNegativeButton("取消", null);

		builder.show();
	}

}
