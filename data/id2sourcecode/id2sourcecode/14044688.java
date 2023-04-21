    public String sendSOAPRequestViaHttpURLConnection(String urlString, String inputFileName) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("SOAPAction", "");
        conn.setInstanceFollowRedirects(false);
        conn.setUseCaches(false);
        DataOutputStream printout = new DataOutputStream(conn.getOutputStream());
        StringBuffer xmlBuffer = new StringBuffer();
        {
            BufferedReader xmlReader = new BufferedReader(new FileReader(inputFileName));
            String tmp;
            while (null != ((tmp = xmlReader.readLine()))) {
                xmlBuffer.append(tmp);
            }
            xmlReader.close();
        }
        printout.writeBytes(xmlBuffer.toString());
        printout.flush();
        printout.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer buff = new StringBuffer();
        String str2;
        while (null != ((str2 = input.readLine()))) {
            buff.append(str2);
        }
        input.close();
        return buff.toString();
    }
