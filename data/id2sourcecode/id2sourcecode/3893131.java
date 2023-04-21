    public static String send() {
        String returnstring;
        returnstring = null;
        if (server == null) {
            System.out.println("sendsms.server value not set");
            return returnstring;
        }
        url_str = server + "?";
        setvar("user", user);
        setvar("password", password);
        setvar("phonenumber", phonenumber);
        setvar("text", text);
        setvar("data", data);
        setvar("udh", udh);
        setvar("pid", pid);
        setvar("dcs", dcs);
        setvar("sender", sender);
        setvar("validity", validity);
        setvar("servicetype", servicetype);
        setvar("smscroute", smscroute);
        setvar("receiptrequested", receiptrequested);
        setvar("sourceport", sourceport);
        setvar("destport", destport);
        setvar("delayuntil", delayuntil);
        setvar("voicemail", voicemail);
        setvar("wapurl", wapurl);
        setvar("wapsl", wapsl);
        try {
            URL url2 = new URL(url_str);
            HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);
            String res = connection.getResponseMessage();
            System.out.println("Response Code  ->" + res);
            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String str;
                while (null != ((str = in.readLine()))) {
                    if (str.startsWith("MessageID=")) {
                        returnstring = returnstring + str + "\r\n";
                        System.out.println(str);
                    }
                }
                connection.disconnect();
            }
        } catch (IOException e) {
            System.out.println("unable to create new url" + e.getMessage());
        }
        return returnstring;
    }
