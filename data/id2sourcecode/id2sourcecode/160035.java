    private LinkedList<String> httprequest(String action, HashMap<String, String> parameters) throws SmsLibException {
        HttpURLConnection conn = null;
        try {
            String line = server + "?username=" + username + "&password=" + password + "&action=" + action;
            for (String key : parameters.keySet()) {
                line = line + "&" + key + "=" + parameters.get(key);
            }
            URL url = new URL(line);
            conn = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            throw new SmsLibException(SmsLibException.Code.COULD_NOT_CONNECT_SERVER_ERROR, "Could not connect to server");
        }
        try {
            conn.connect();
        } catch (IOException e) {
            throw new SmsLibException(SmsLibException.Code.COULD_NOT_CONNECT_REQUEST_ERROR, "Could not connect to server");
        }
        try {
            int ret = conn.getResponseCode();
            if (ret != 200) {
                throw new SmsLibException(ret);
            }
        } catch (IOException e) {
            throw new SmsLibException(SmsLibException.Code.COULD_NOT_READ_STATUS_CODE, "Could not read status code");
        }
        BufferedReader rd;
        try {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            throw new SmsLibException(SmsLibException.Code.NO_ANSWER_ERROR, "Did not recive answer");
        }
        LinkedList<String> retParam = new LinkedList<String>();
        try {
            String line;
            while ((line = rd.readLine()) != null) {
                retParam.add(line);
            }
        } catch (IOException e) {
            throw new SmsLibException(SmsLibException.Code.BUFFERD_READ_ERROR, "Could not read from BufferdReader. This should not happen");
        }
        if (retParam.size() == 1) {
            try {
                Integer code = Integer.decode(retParam.get(0).substring(0, 3));
                if (code.intValue() != 200) {
                    throw new SmsLibException(code);
                }
            } catch (NumberFormatException error) {
            }
        }
        return retParam;
    }
