    public String send() throws Exception {
        URL url = new URL(config.getHost().getURL());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                System.out.println(hostname);
                return true;
            }
        });
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        connection.setRequestProperty("Accept", "text/xml");
        byte[] query = getXML().getBytes("UTF-8");
        int queryLength = query.length;
        connection.setRequestProperty("Content-length", String.valueOf(queryLength));
        OutputStream out = connection.getOutputStream();
        out.write(query);
        System.out.println("Sending " + queryLength + " bytes");
        System.out.println("Resp Code:" + connection.getResponseCode());
        System.out.println("Resp Message:" + connection.getResponseMessage());
        System.out.println("====");
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        InputStream in = connection.getInputStream();
        FileOutputStream fOut = new FileOutputStream("out.xml");
        int ch = 0;
        while ((ch = in.read()) >= 0) {
            fOut.write(ch);
            bOut.write(ch);
        }
        fOut.close();
        bOut.close();
        return bOut.toString("UTF-8");
    }
