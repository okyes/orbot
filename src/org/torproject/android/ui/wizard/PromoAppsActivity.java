package org.torproject.android.ui.wizard;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.torproject.android.OrbotConstants;
import org.torproject.android.R;

import java.util.List;

public class PromoAppsActivity extends Activity implements OrbotConstants {

    final static String MARKET_URI = "market://details?id=";
    final static String FDROID_APP_URI = "https://f-droid.org/repository/browse/?fdid=";
    final static String PLAY_APP_URI = "https://play.google.com/store/apps/details?id=";
    final static String FDROID_URI = "https://f-droid.org/repository/browse/?fdfilter=info.guardianproject";
    final static String PLAY_URI = "https://play.google.com/store/apps/developer?id=The+Guardian+Project";

    private final static String FDROID_PACKAGE_NAME = "org.fdroid.fdroid";
    private final static String PLAY_PACKAGE_NAME = "com.android.vending";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {

        super.onStart();
        setContentView(R.layout.layout_wizard_tips);

        stepFive();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    void stepFive(){


        String title = getString(R.string.wizard_tips_title);

        setTitle(title);

        Button btnLink = (Button)findViewById(R.id.WizardRootButtonInstallGibberbot);

        btnLink.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
                startActivity(getInstallIntent("info.guardianproject.otr.app.im"));

            }
        });

        btnLink = (Button)findViewById(R.id.WizardRootButtonInstallOrweb);

        btnLink.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
                startActivity(getInstallIntent("info.guardianproject.browser"));

            }
        });

        btnLink = (Button)findViewById(R.id.WizardRootButtonInstallDuckgo);

        btnLink.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                finish();
                startActivity(getInstallIntent("com.duckduckgo.mobile.android"));

            }
        });

        btnLink = (Button)findViewById(R.id.WizardRootButtonInstallTwitter);

        btnLink.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                String url = getString(R.string.twitter_setup_url);
                finish();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

            }
        });

        btnLink = (Button)findViewById(R.id.WizardRootButtonInstallStoryMaker);

        btnLink.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
                startActivity(getInstallIntent("info.guardianproject.mrapp"));

            }
        });

        btnLink = (Button)findViewById(R.id.WizardRootButtonInstallMartus);

        btnLink.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
                startActivity(getInstallIntent("org.martus.android"));

            }
        });

        btnLink = (Button)findViewById(R.id.WizardRootButtonGooglePlay);
        PackageManager pm = getPackageManager();
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        // change text and icon based on which app store is installed (or not)
        try {
            if (isAppInstalled(pm, FDROID_PACKAGE_NAME)) {
                Drawable icon = pm.getApplicationIcon(FDROID_PACKAGE_NAME);
                btnLink.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                btnLink.setText(R.string.wizard_tips_fdroid);
                intent.setPackage(FDROID_PACKAGE_NAME);
                intent.setData(Uri.parse(FDROID_URI));
            } else if (isAppInstalled(pm, PLAY_PACKAGE_NAME)) {
                Drawable icon = pm.getApplicationIcon(PLAY_PACKAGE_NAME);
                btnLink.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                btnLink.setText(R.string.wizard_tips_play);
                intent.setPackage(PLAY_PACKAGE_NAME);
                intent.setData(Uri.parse(PLAY_URI));
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            btnLink.setText(R.string.wizard_tips_fdroid_org);
            intent.setData(Uri.parse(FDROID_URI));
        }

        btnLink.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });


      //  Button back = ((Button)findViewById(R.id.btnWizard1));
        Button next = ((Button)findViewById(R.id.btnWizard2));

        /*
        back.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
				startActivityForResult(new Intent(PromoAppsActivity.this, ChooseLocaleWizardActivity.class), 1);
			}
		});*/

    	next.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//showWizardFinal();

				finish();
			}
		});

	}



	//Code to override the back button!
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if(keyCode == KeyEvent.KEYCODE_BACK){
	    	finish();
	    	startActivity(new Intent(getBaseContext(), ChooseLocaleWizardActivity.class));
	    	return true;
	    }
	    return false;
	}

    boolean isAppInstalled(PackageManager pm, String packageName) {
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    Intent getInstallIntent(String packageName) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(MARKET_URI + packageName));

        PackageManager pm = getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(intent, 0);

        String foundPackageName = null;
        for (ResolveInfo r : resInfos) {
            Log.i(TAG, "market: " + r.activityInfo.packageName);
            if (TextUtils.equals(r.activityInfo.packageName, FDROID_PACKAGE_NAME)
                    || TextUtils.equals(r.activityInfo.packageName, PLAY_PACKAGE_NAME)) {
                foundPackageName = r.activityInfo.packageName;
                break;
            }
        }

        if (foundPackageName == null) {
            intent.setData(Uri.parse(FDROID_APP_URI + packageName));
        } else {
            intent.setPackage(foundPackageName);
        }
        return intent;
    }

    /*
	private void showWizardFinal ()
	{
		String title = null;
		String msg = null;


		title = context.getString(R.string.wizard_final);
		msg = context.getString(R.string.wizard_final_msg);

		DialogInterface.OnClickListener ocListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.startActivity(new Intent(context, Orbot.class));

			}
		};


		 new AlertDialog.Builder(context)
		.setIcon(R.drawable.icon)
        .setTitle(title)
        .setPositiveButton(R.string.button_close, ocListener)
        .setMessage(msg)
        .show();
    }*/
}
