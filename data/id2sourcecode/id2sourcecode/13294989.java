    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case MENU_BAD_API_VERSION:
                return new AlertDialog.Builder(this).setTitle(R.string.bad_api_version).setMessage(R.string.need_upgrade).setCancelable(false).setPositiveButton(android.R.string.ok, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:net.sylvek.where")));
                    }
                }).setNegativeButton(android.R.string.cancel, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
            case MENU_IMPORT_FRIENDS:
                return new AlertDialog.Builder(this).setTitle(R.string.import_friends).setMessage(R.string.sure_to_import).setCancelable(false).setPositiveButton(android.R.string.yes, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        launchImportFriends();
                    }
                }).setNegativeButton(android.R.string.no, null).create();
            case MENU_RESET:
                return new AlertDialog.Builder(this).setTitle(R.string.reset).setMessage(R.string.sure_to_delete).setPositiveButton(android.R.string.yes, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            SylvekClient.delete(FbClient.ID);
                            pref.edit().remove(STORED_ACCESS_TOKEN).remove(STORED_ID).remove(STORED_NAME).commit();
                        } catch (Exception e) {
                            Log.e("delete.user", e.getMessage());
                        }
                        showDialog(MENU_CONNECTION);
                    }
                }).setNegativeButton(android.R.string.no, null).create();
            case MENU_CONNECTION:
                Builder connectBuilder = new AlertDialog.Builder(Where.this).setCancelable(false);
                View connect = LayoutInflater.from(this).inflate(R.layout.connect, null);
                final Button getAbout = (Button) connect.findViewById(R.id.get_help);
                getAbout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showDialog(MENU_ABOUT);
                    }
                });
                connectBuilder.setNegativeButton(R.string.quit, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                connectBuilder.setPositiveButton(R.string.validate, new OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        showDialog(MENU_REGISTRATION);
                    }
                });
                connectBuilder.setTitle(R.string.app_name);
                connectBuilder.setView(connect);
                return connectBuilder.setCancelable(false).create();
            case MENU_REGISTRATION:
                WebView facebook = new WebView(this) {

                    @Override
                    public boolean onCheckIsTextEditor() {
                        return true;
                    }
                };
                final AlertDialog dlg = new AlertDialog.Builder(this).setView(facebook).create();
                facebook.setWebChromeClient(new WebChromeClient() {

                    @Override
                    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                        if (url.startsWith(FbClient.URL_REDIRECT)) {
                            dlg.cancel();
                            try {
                                HttpClient client = new DefaultHttpClient();
                                String target = FbClient.URL_ACCESS_TOKEN + URLEncoder.encode(message);
                                HttpResponse response = client.execute(new HttpGet(target));
                                if (response.getStatusLine().getStatusCode() == 200) {
                                    String access_token = EntityUtils.toString(response.getEntity());
                                    if (access_token.startsWith(FbClient.ACCESS_TOKEN_KEY)) {
                                        String accessToken = FbClient.parseAccessToken(access_token);
                                        Log.d("access_token", accessToken);
                                        facebookClient = new DefaultFacebookClient(accessToken);
                                        User me = facebookClient.fetchObject("me", User.class, Parameter.with("fields", "id, name"));
                                        FbClient.ID = me.getId();
                                        FbClient.NAME = me.getName();
                                        pref.edit().putString(STORED_ACCESS_TOKEN, accessToken).putString(STORED_ID, FbClient.ID).putString(STORED_NAME, FbClient.NAME).commit();
                                        launchImportFriends();
                                    }
                                } else {
                                    showDialog(MENU_REGISTRATION);
                                }
                            } catch (Exception e) {
                                Log.e("get.access_token", e.getMessage());
                                Toast.makeText(Where.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                showDialog(MENU_REGISTRATION);
                            }
                            return true;
                        }
                        return false;
                    }
                });
                facebook.setWebViewClient(new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                facebook.getSettings().setJavaScriptEnabled(true);
                facebook.loadUrl(FbClient.URL_AUTH + FbClient.API_KEY);
                return dlg;
            case MENU_ABOUT:
                WebView webview = new WebView(this);
                webview.loadUrl(SylvekClient.HELP_WEB_PAGE);
                webview.setBackgroundColor(0);
                return new AlertDialog.Builder(this).setTitle(R.string.app_about).setView(webview).create();
        }
        return null;
    }
