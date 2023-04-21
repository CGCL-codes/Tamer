            public void run(AccountManagerFuture<Bundle> result) {
                Bundle bundle;
                try {
                    bundle = result.getResult();
                    Intent intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
                    if (intent != null) {
                        mainActivity.startActivity(intent);
                    } else {
                        String auth_token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                        http_client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
                        HttpGet http_get = new HttpGet("http://3dforandroid.appspot.com/_ah" + "/login?continue=http://localhost/&auth=" + auth_token);
                        HttpResponse response = http_client.execute(http_get);
                        if (response.getStatusLine().getStatusCode() != 302) return;
                        for (Cookie cookie : http_client.getCookieStore().getCookies()) {
                            if (cookie.getName().equals("ACSID")) {
                                authClient = http_client;
                                new ReadNote(mainActivity, nosql, list).execute(http_client);
                            }
                        }
                    }
                } catch (OperationCanceledException e) {
                    e.printStackTrace();
                } catch (AuthenticatorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    http_client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
                }
                return;
            }
