    public void _testUrlConnectionCC() throws Exception {
        HttpURLConnection connection = null;
        File doc = new File("C:\\TIC_LOCAL\\EclipseProjects\\OpenSourceRacsor\\cc_request\\jmeter\\doJob_car_Fairview.binary");
        byte[] theFile = FileUtils.readFileToByteArray(doc);
        String urlString = "http://ct-google.crimecitygame.com/ct-google/index.php/amf_gateway?f=104995124568475809013&r=43&t=none";
        String content = "application/x-amf";
        URL url = new URL(urlString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Type", content);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(theFile);
        wr.flush();
        wr.close();
        System.out.println("responsecode:" + connection.getResponseCode());
        InputStream is = connection.getInputStream();
        UtilsFlexMessage deserializer = new UtilsFlexMessage();
        deserializer.parseInputStream(is);
    }
