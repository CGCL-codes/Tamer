    private HttpClient LoginToElearning(int nAttemptedLogins, HttpClient client) {
        try {
            if (nAttemptedLogins % 3 == 0) {
                Thread.currentThread();
                Thread.sleep(2000);
            }
            client = new DefaultHttpClient();
            HttpGet request1 = new HttpGet("https://elearning.utdallas.edu/webct/entryPageIns.dowebct");
            Log.i(LOG_CONNECTION, "-- about to execute first request to eLearning --");
            HttpResponse response1 = client.execute(request1);
            HttpEntity entity1 = response1.getEntity();
            if (entity1 == null) {
                Log.e(LOG_LOGINPROCESS, "error in connecting.. 1");
                if (nAttemptedLogins < LOGIN_ATTEMPTS) return LoginToElearning(++nAttemptedLogins, client); else {
                    this.LogoutElearning(client);
                    return null;
                }
            }
            String sHTML1 = this.parseHtmlFromEntity(entity1);
            if (sHTML1.length() == 0) {
                Log.e(LOG_LOGINPROCESS, "error parsing HTML1");
                if (nAttemptedLogins < LOGIN_ATTEMPTS) return LoginToElearning(++nAttemptedLogins, client); else {
                    this.LogoutElearning(client);
                    return null;
                }
            }
            ArrayList<BasicNameValuePair> listParams1 = null;
            int nIndex = sHTML1.indexOf("Academic");
            if (nIndex > 0) {
                listParams1 = this.parseNameValueFromText(nIndex, "name =\"", "\"", "value =\"", "\"", sHTML1, 3);
            }
            if (listParams1 == null) {
                Log.e(LOG_LOGINPROCESS, "error parsing parameters");
                if (nAttemptedLogins < LOGIN_ATTEMPTS) return LoginToElearning(++nAttemptedLogins, client); else {
                    this.LogoutElearning(client);
                    return null;
                }
            }
            for (int i = 0; i < listParams1.size(); i++) Log.d(LOG_CONNECTION, "R1: name: " + listParams1.get(i).getName() + "  value: " + listParams1.get(i).getValue());
            response1 = null;
            sHTML1 = "";
            Log.i(LOG_CONNECTION, " -- About to make second call -- ");
            HttpPost httpost = new HttpPost("https://elearning.utdallas.edu/webct/entryPage.dowebct");
            httpost.setEntity(new UrlEncodedFormEntity(listParams1, HTTP.UTF_8));
            HttpResponse response2 = client.execute(httpost);
            HttpEntity entity2 = response2.getEntity();
            if (entity2 == null) {
                Log.e(LOG_LOGINPROCESS, "error on second page response");
                if (nAttemptedLogins < LOGIN_ATTEMPTS) return LoginToElearning(++nAttemptedLogins, client); else {
                    this.LogoutElearning(client);
                    return null;
                }
            }
            Log.i(LOG_CONNECTION, "-- about to parse response2 to get parameters --");
            sHTML1 = this.parseHtmlFromEntity(entity2);
            listParams1 = new ArrayList<BasicNameValuePair>();
            nIndex = sHTML1.indexOf("function submitLogin()");
            if (nIndex > 0) {
                listParams1 = this.parseNameValueFromText(nIndex, "document.vistaInsEntryForm.", ".", "value = \"", "\"", sHTML1, 4);
            } else {
                Log.e(LOG_LOGINPROCESS, "error making it to the second page, couldn't find the submitLogin func on the html loaded");
                if (nAttemptedLogins < LOGIN_ATTEMPTS) return LoginToElearning(++nAttemptedLogins, client); else {
                    this.LogoutElearning(client);
                    return null;
                }
            }
            if (listParams1 == null) {
                Log.e(LOG_LOGINPROCESS, "error parsing params from second page for third page");
                if (nAttemptedLogins < LOGIN_ATTEMPTS) return LoginToElearning(++nAttemptedLogins, client); else {
                    this.LogoutElearning(client);
                    return null;
                }
            }
            for (int i = 0; i < listParams1.size(); i++) Log.d(LOG_CONNECTION, "R2: name: " + listParams1.get(i).getName() + "  value: " + listParams1.get(i).getValue());
            HttpPost httpost3 = new HttpPost("https://elearning.utdallas.edu/webct/logonDisplay.dowebct");
            Log.i(LOG_CONNECTION, " -- About to make third call -- ");
            httpost3.setEntity(new UrlEncodedFormEntity(listParams1, HTTP.UTF_8));
            HttpResponse response3 = client.execute(httpost3);
            HttpEntity entity3 = response3.getEntity();
            if (entity3 == null) {
                Log.e(LOG_LOGINPROCESS, "error on third page response");
                if (nAttemptedLogins < LOGIN_ATTEMPTS) return LoginToElearning(++nAttemptedLogins, client); else {
                    this.LogoutElearning(client);
                    return null;
                }
            }
            nIndex = this.parseHtmlFromEntity(entity3).indexOf("<title>Log in to eLearning</title>");
            if (nIndex < 0) {
                Log.e(LOG_LOGINPROCESS, "error on third page response - 2");
                if (nAttemptedLogins < LOGIN_ATTEMPTS) return LoginToElearning(++nAttemptedLogins, client); else {
                    this.LogoutElearning(client);
                    return null;
                }
            }
            listParams1.add(new BasicNameValuePair("gotoid", "null"));
            listParams1.add(new BasicNameValuePair("timeZoneOffset", "6"));
            listParams1.add(new BasicNameValuePair("webctid", getUsername()));
            listParams1.add(new BasicNameValuePair("password", getPassword()));
            for (int i = 0; i < listParams1.size(); i++) Log.d(LOG_CONNECTION, "R3: name: " + listParams1.get(i).getName() + "  value: " + listParams1.get(i).getValue());
            Log.i(LOG_CONNECTION, " -- About to make fourth call -- actual login with user/pass --  ");
            HttpPost httpost4 = new HttpPost("https://elearning.utdallas.edu/webct/authenticateUser.dowebct");
            httpost4.setEntity(new UrlEncodedFormEntity(listParams1, HTTP.UTF_8));
            HttpResponse response4 = client.execute(httpost4);
            HttpEntity entity4 = response4.getEntity();
            if (entity4 == null) {
                Log.e(LOG_LOGINPROCESS, "error on fourth page response");
                if (nAttemptedLogins < LOGIN_ATTEMPTS) return LoginToElearning(++nAttemptedLogins, client); else {
                    this.LogoutElearning(client);
                    return null;
                }
            }
            nIndex = this.parseHtmlFromEntity(entity4).indexOf("<title>Blackboard Learning System</title>");
            if (nIndex < 0) {
                Log.e(LOG_LOGINPROCESS, "error on fourth page response - 2");
                if (nAttemptedLogins < LOGIN_ATTEMPTS) return LoginToElearning(++nAttemptedLogins, client); else {
                    this.LogoutElearning(client);
                    return null;
                }
            }
            Log.i(LOG_CONNECTION, "-- successfully logged in to eLearning --");
        } catch (Exception ex) {
            Log.e(LOG_CONNECTION, "LoginToElearning error: " + ex.getMessage());
            return null;
        }
        return client;
    }
