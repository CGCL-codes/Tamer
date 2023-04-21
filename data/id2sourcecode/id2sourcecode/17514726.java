    private void addButtonListener() {
        Button confirm = (Button) this.findViewById(R.id.login_confirm);
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("m", "login"));
                nameValuePairs.add(new BasicNameValuePair("c", "User"));
                nameValuePairs.add(new BasicNameValuePair("password", "cloudisgreat"));
                nameValuePairs.add(new BasicNameValuePair("alias", "cs588"));
                String result = "";
                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    result = EntityUtils.toString(response.getEntity());
                    String info[] = result.split(",");
                    String info1 = info[0].trim();
                    String loginResult[] = info1.split(":");
                    String lResult = loginResult[1];
                    Header[] acturalHeaders = response.getAllHeaders();
                    String sessionId = substractSessionId(acturalHeaders);
                    Log.d("SessionId in the Header:", sessionId);
                    if (lResult.equals("0")) {
                        Intent i = new Intent(LoginActivity.this, DestopActivity.class);
                        i.putExtra(Constant.PHP_SESSION_ID, sessionId);
                        startActivity(i);
                    } else if (lResult.equals("-1")) {
                        welcome.setText(Constant.LOGIN_ERROR_MESSAGE);
                    }
                    Log.d("Cloud Debug", lResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    result = e.getMessage();
                }
                Log.d("MSG", result);
            }
        });
        Button clear = (Button) this.findViewById(R.id.login_clear);
        clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                login.setText("");
                psw.setText("");
            }
        });
        Button reg = (Button) this.findViewById(R.id.register);
        reg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
