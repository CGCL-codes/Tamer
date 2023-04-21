            public void run() {
                InputStream content = null;
                boolean loggedin = false;
                try {
                    Log.d("http", "Start httpPost");
                    List nameValuePairs = new ArrayList(2);
                    nameValuePairs.add(new BasicNameValuePair("form_username", username));
                    nameValuePairs.add(new BasicNameValuePair("form_password", password));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                    httpPost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
                    Log.d("http", "get response");
                    HttpResponse response = httpclient.execute(httpPost);
                    content = response.getEntity().getContent();
                    Log.d("http", "read out response");
                    response.getStatusLine().getStatusCode();
                    cookiesArray = getCookies(response);
                    Iterator iterator = cookiesArray.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        settingsEdit.putString("cookie_" + key, "[" + cookiesArray.get(key).getValue() + "]" + "[" + cookiesArray.get(key).getDomain() + "]");
                    }
                    settingsEdit.commit();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(content), 4096);
                    String line;
                    while (((line = rd.readLine()) != null) && loggedin != true) {
                        if (line.contains("href=\"/usercp\"")) {
                            loggedin = true;
                            break;
                        }
                    }
                    rd.close();
                } catch (Exception e) {
                }
                requestActionListener.onLoggedIn(loggedin, username);
            }
