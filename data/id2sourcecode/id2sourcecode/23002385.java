    public void httpGet() throws Exception {
        Properties systemSettings = System.getProperties();
        systemSettings.put("proxySet", "true");
        systemSettings.put("http.proxyHost", "192.168.80.3");
        systemSettings.put("http.proxyPort", "8080");
        URL url = new URL("http://www.baidu.com");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setRequestMethod("GET");
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String encodedUserPwd = encoder.encode("ncs\\wenwei:Moto*1234".getBytes());
        httpCon.setRequestProperty("Proxy-Authorization", "Basic " + encodedUserPwd);
        httpCon.setRequestMethod("HEAD");
        long date = httpCon.getDate();
        if (date == 0) {
            System.out.println("No date information.");
        } else {
            System.out.println("Date: " + new Date(date));
        }
        System.out.println(httpCon.getResponseCode() + " : " + httpCon.getResponseMessage());
    }
