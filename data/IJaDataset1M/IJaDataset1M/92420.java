package com.diipo.weibo;

import com.diipo.weibo.entity.Users;
import com.diipo.weibo.utils.HttpUtils;
import com.diipo.weibo.utils.JsonUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends Activity {

    private String TAG = UserInfoActivity.class.getSimpleName();

    private SharedPreferences mPrefs;

    private String mUserName;

    private String mUrl_users;

    private Users mUser = new Users();

    private TextView mTitle;

    private Button mBack;

    private Button mReply;

    private Context mctx;

    private Intent mIntent;

    private TextView mName;

    private ImageView mIcon;

    private LinearLayout mEmailBar;

    private LinearLayout mTelephoneBar;

    private TextView mTextEmail;

    private TextView mTextTelephone;

    private ImageView mDivider1;

    private ImageView mDivider2;

    private ImageView mDivider3;

    private ImageView mEmailIcon;

    private ImageView mTelIcon;

    private int CALL_PHONE = 1;

    private int EMAIL_MSG = 2;

    private int SEND_LETTER = 3;

    private AsyncImageLoader asyncImageLoader = AsyncImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.users_view);
        mDivider1 = (ImageView) findViewById(R.id.details_view_divider);
        mDivider2 = (ImageView) findViewById(R.id.details_view_divider2);
        mDivider3 = (ImageView) findViewById(R.id.details_view_divider3);
        mEmailIcon = (ImageView) findViewById(R.id.email_icon);
        mTelIcon = (ImageView) findViewById(R.id.telephone_icon);
        mName = (TextView) findViewById(R.id.user_name);
        mIcon = (ImageView) findViewById(R.id.user_icon);
        mEmailBar = (LinearLayout) findViewById(R.id.email_bar);
        mEmailBar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e(" pyb ", " email bar was click .....  ");
                showDialog(EMAIL_MSG);
            }
        });
        mEmailBar.setVisibility(View.GONE);
        mTelephoneBar = (LinearLayout) findViewById(R.id.telephone_bar);
        mTelephoneBar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e(" pyb ", " telephone bar was click .....  ");
                showDialog(CALL_PHONE);
            }
        });
        mTelephoneBar.setVisibility(View.GONE);
        mTextEmail = (TextView) findViewById(R.id.user_email);
        mTextTelephone = (TextView) findViewById(R.id.user_telephone);
        mctx = this;
        mTitle = (TextView) findViewById(R.id.titletext);
        mTitle.setText(mctx.getString(R.string.user_detail));
        mBack = (Button) findViewById(R.id.btn_title_back);
        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mReply = (Button) findViewById(R.id.btn_title_reply);
        mReply.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(SEND_LETTER);
            }
        });
        mIntent = getIntent();
        mUserName = mIntent.getStringExtra(ConfigInfo.COMM.USER_NAME);
        mUrl_users = "/api/users/" + mUserName + ".json?api_key=";
        mPrefs = getSharedPreferences(ConfigInfo.USER_PREFERENCES, MODE_PRIVATE);
        new AsyncDataLoader(asyncRemoteCallback, this, mUrl_users, "", "", "").execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, " onResume() called...... ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, " onStop() called...... ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, " onDestroy() called...... ");
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case 1:
                return new AlertDialog.Builder(UserInfoActivity.this).setTitle(R.string.is_tel).setMessage(mUser.getTel()).setPositiveButton(R.string.call_phone, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + mUser.getTel()));
                        startActivity(phoneIntent);
                    }
                }).setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create();
            case 2:
                return new AlertDialog.Builder(UserInfoActivity.this).setTitle(R.string.is_email).setMessage(mUser.getEmail()).setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { mUser.getEmail() });
                        startActivity(Intent.createChooser(emailIntent, "选择邮件客户端"));
                    }
                }).setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create();
            case 3:
                return new AlertDialog.Builder(UserInfoActivity.this).setItems(R.array.users_reply_type_items, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(" pyb ", " approve final  which = " + which);
                        switch(which) {
                            case 0:
                                Intent i = new Intent(UserInfoActivity.this, CreateWeiBoActivity.class);
                                i.putExtra(ConfigInfo.COMM.SEND_TYPE, ConfigInfo.COMM.WEIBO_MSG);
                                i.putExtra(ConfigInfo.COMM.DIRECT_MENTION, mUser.getUsername());
                                startActivity(i);
                                break;
                            case 1:
                                Intent intent = new Intent(UserInfoActivity.this, CreateWeiBoActivity.class);
                                intent.putExtra(ConfigInfo.COMM.SEND_TYPE, ConfigInfo.COMM.WEIBO_LETTER);
                                intent.putExtra(ConfigInfo.COMM.SEND_LETTER, mUser.getUsername());
                                intent.putExtra(ConfigInfo.COMM.USER_ID, mUser.getId());
                                startActivity(intent);
                                break;
                            case 2:
                                break;
                            default:
                                break;
                        }
                    }
                }).create();
        }
        return null;
    }

    /**
	 * 异步加载服务器上的微博数据的监听器,已查看和未查看 消息
	 */
    private AsyncDataLoader.Callback asyncRemoteCallback = new AsyncDataLoader.Callback() {

        String mResult;

        @Override
        public void onStart(String address, String more) {
            StringBuffer url = new StringBuffer();
            url.append(mPrefs.getString(ConfigInfo.PREF_SERVER_ADDRESS, mPrefs.getString(ConfigInfo.PREF_SERVER_ADDRESS, ConfigInfo.COMM.CONST_SERVER_ADDRESS)));
            url.append(address);
            url.append(ConfigInfo.COMM.APY_KEY);
            String username = mPrefs.getString(ConfigInfo.PREF_USERNAME, "");
            String passwd = mPrefs.getString(ConfigInfo.PREF_PASSWORD, "");
            mResult = HttpUtils.doPost(url.toString(), username, passwd, null);
            Log.e("", " users mResult =  " + mResult);
            if (mResult != ConfigInfo.HTTPRETURN.COMMHTTPERRORS && mResult != ConfigInfo.HTTPRETURN.HTTP_ERROR_400 && mResult != ConfigInfo.HTTPRETURN.HTTP_ERROR_401 && mResult != ConfigInfo.HTTPRETURN.HTTPERROR) {
                try {
                    mUser = JsonUtils.parseUserFromJson(mResult, Users.class);
                    Log.e("", " mUser  =  " + mUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, " get no data from the server ");
            }
        }

        @Override
        public void onStart(String address, String arg1, String arg2) {
        }

        @Override
        public void onFinish() {
            if (mResult == ConfigInfo.HTTPRETURN.COMMHTTPERRORS || mResult == ConfigInfo.HTTPRETURN.HTTP_ERROR_400 || mResult == ConfigInfo.HTTPRETURN.HTTP_ERROR_401 || mResult == ConfigInfo.HTTPRETURN.HTTPERROR || mResult == ConfigInfo.HTTPRETURN.HTTP_ERROR_CODE_404) {
                Toast.makeText(UserInfoActivity.this, mResult, Toast.LENGTH_LONG).show();
                return;
            }
            Log.e("", " error mResult  =  " + mResult);
            mName.setText(mUser.getUsername());
            asyncImageLoader.loadPortrait(mUser.getProfile_picture(), mIcon);
            mDivider1.setVisibility(View.VISIBLE);
            if (mUser.getEmail().equals("")) {
                mEmailBar.setVisibility(View.GONE);
            } else {
                mEmailBar.setVisibility(View.VISIBLE);
                mTextEmail.setText(mUser.getEmail());
                mDivider2.setVisibility(View.VISIBLE);
            }
            if (mUser.getTel().equals("")) {
                mTelephoneBar.setVisibility(View.GONE);
            } else {
                mTelephoneBar.setVisibility(View.VISIBLE);
                mTextTelephone.setText(mUser.getTel());
                mDivider3.setVisibility(View.VISIBLE);
            }
            Log.e("", " mUser getEmail =  " + mUser.getEmail());
            Log.e("", " mUser mTextTelephone =  " + mUser.getTel());
        }

        @Override
        public void onPrepare() {
        }
    };
}
