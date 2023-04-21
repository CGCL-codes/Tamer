    public RestConnection(String baseUrl, String[][] pathParams, String[][] params) {
        try {
            String urlStr = baseUrl;
            if (pathParams != null && pathParams.length > 0) {
                urlStr = replaceTemplateParameters(baseUrl, pathParams);
            }
            URL url = new URL(encodeUrl(urlStr, params));
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            conn.setAllowUserInteraction(true);
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
            date = format.format(new Date());
            conn.setRequestProperty("Date", date);
        } catch (Exception ex) {
            Logger.getLogger(RestConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
