    private static URLConnection openConnection(String urlString, String user, String pass, final int method, String content, boolean robustMode) throws MalformedURLException, IOException {
        try {
            Thread.sleep(Constants.REST_DELAY);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        URL url = new URL(urlString);
        boolean auth = false;
        String encoded = null;
        if (auth = (user != null && pass != null && !"".equals(user) && !"".equals(pass))) {
            String userPassword = user + ":" + pass;
            encoded = Base64Utils.toBase64(userPassword.getBytes());
        }
        URLConnection uc = null;
        OutputStreamWriter out = null;
        try {
            uc = url.openConnection();
            if (auth) {
                uc.setRequestProperty("Authorization", "Basic " + encoded);
            }
            switch(method) {
                case GET:
                    break;
                case PUT:
                    uc.setDoOutput(true);
                    ((HttpURLConnection) uc).setRequestMethod("PUT");
                    ((HttpURLConnection) uc).setRequestProperty("Content-type", "text/xml; charset=" + "UTF-8");
                    try {
                        out = new OutputStreamWriter(uc.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return uc;
                    }
                    try {
                        if (content != null) out.write(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return uc;
                    }
                    out.flush();
                    break;
                case POST:
                    uc.setDoOutput(true);
                    uc.setDoInput(true);
                    ((HttpURLConnection) uc).setRequestMethod("POST");
                    ((HttpURLConnection) uc).setRequestProperty("Content-type", "text/xml; charset=" + "UTF-8");
                    try {
                        out = new OutputStreamWriter(uc.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return uc;
                    }
                    try {
                        if (content != null) out.write(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return uc;
                    }
                    out.flush();
                    break;
                case DELETE:
                    uc.setDoOutput(true);
                    uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    ((HttpURLConnection) uc).setRequestMethod("DELETE");
                    break;
            }
            int resp = ((HttpURLConnection) uc).getResponseCode();
            if (resp < 200 || resp >= 308) {
                if (robustMode) {
                    return null;
                } else {
                    if (uc != null) LOGGER.error(convertStreamToString(uc.getInputStream()));
                    LOGGER.error("Unable to open connection on " + urlString + "  response code: " + resp);
                    throw new ConnectionException("connection to " + urlString + " cannot be established");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConnectionException("connection to " + urlString + " cannot be established");
        }
        return uc;
    }
