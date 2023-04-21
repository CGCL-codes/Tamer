    public String get(final String _url) throws Exception {
        url = new URL(_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(false);
        if (authorization != null) connection.setRequestProperty("Authorization", authorization);
        int responseLen = connection.getContentLength();
        InputStream ins = null;
        try {
            ins = connection.getInputStream();
        } catch (IOException ioe) {
            if (ins != null) ins.close();
            StringBuffer _err = new StringBuffer();
            StackTraceElement _st[] = ioe.getStackTrace();
            _err.append("HTTPClient: ").append(url.toString()).append(" Response code ").append(connection.getResponseCode()).append(" ").append(connection.getResponseMessage()).append("\n");
            for (int _i = 0; _i < 10 && _i < _st.length; _i++) _err.append(_st[_i]).append("\n");
            logger.error(_err.toString());
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) response.append(inputLine);
        in.close();
        return response.toString();
    }
