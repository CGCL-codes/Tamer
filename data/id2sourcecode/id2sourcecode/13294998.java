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
