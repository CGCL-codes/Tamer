    private static Map<String, String> execPayPalNVPCall(final Map<String, String> params, final PPECheckoutLocDepConfiguration config) {
        if (params == null) {
            throw new NullPointerException("The given parameter-map is null.");
        }
        if (params.isEmpty()) {
            throw new IllegalArgumentException("The given parameter-map is empty.");
        }
        if (config == null) {
            throw new NullPointerException("The given PPECheckoutLocDepConfiguration is null.");
        }
        StringBuilder urlBuilder = new StringBuilder();
        HttpsURLConnection connection;
        try {
            urlBuilder.append(config.getPayPalURL());
            urlBuilder.append("?USER=").append(URLEncoder.encode(config.getUsername(), "UTF-8"));
            urlBuilder.append("&PWD=").append(URLEncoder.encode(config.getPassword(), "UTF-8"));
            urlBuilder.append("&SIGNATURE=").append(URLEncoder.encode(config.getSignature(), "UTF-8"));
            urlBuilder.append("&VERSION=").append(URLEncoder.encode(config.getVersion(), "UTF-8"));
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.append('&').append(URLEncoder.encode(entry.getKey().toUpperCase(), "UTF-8"));
                urlBuilder.append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            connection = (HttpsURLConnection) new URL(urlBuilder.toString()).openConnection();
            connection.connect();
        } catch (IOException e) {
            throw new PulseException(e.getLocalizedMessage(), e);
        }
        StringBuilder responseBuilder = new StringBuilder();
        InputStream in = null;
        try {
            in = connection.getInputStream();
            while (in.available() > 0) {
                responseBuilder.append(Character.toChars(in.read()));
            }
        } catch (IOException e) {
            throw new PulseException(e.getLocalizedMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.warn(e.getLocalizedMessage(), e);
                }
            }
            connection.disconnect();
        }
        String response = responseBuilder.toString();
        Map<String, String> result = new HashMap<String, String>();
        for (String param : response.split("&")) {
            String[] tmp = param.split("=");
            try {
                result.put(URLDecoder.decode(tmp[0], "UTF-8"), URLDecoder.decode(tmp[1], "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new PulseException(e.getLocalizedMessage(), e);
            }
        }
        return result;
    }
