    private void publishLang(Schedule persistentSchedule, String publishUrl, String lang) throws Exception {
        final String xmlData = scheduleToXml(persistentSchedule, lang);
        log.info("Publishing schedule lang=" + lang + " to: " + publishUrl);
        URL url = new URL(publishUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        String authStr = "YWRtaW46YWE0c2RXZWI=";
        connection.setRequestProperty("Authorization", "Basic " + authStr);
        final OutputStream os = connection.getOutputStream();
        addParameter(os, "block_template_id", "0");
        addParameter(os, "description", "");
        addParameter(os, "data_process_id", "0");
        addParameter(os, "data_type_id", "5");
        addParameter(os, "is_published", "1");
        addParameter(os, "btnUpdate", "true");
        addParameter(os, "data", xmlData);
        os.close();
        final InputStream is = connection.getInputStream();
        while (is.read() >= 0) {
        }
        is.close();
        connection.disconnect();
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new Exception("Server returned error code: " + connection.getResponseCode());
        }
        log.info("Published successfully");
    }
