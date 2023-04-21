    public static String[] getVersion(URL url) throws ArcImsException {
        String[] getV = new String[2];
        Reader isr;
        URL urlCmd;
        HttpURLConnection conn;
        try {
            urlCmd = new URL(url.toString() + "?" + GetVariables.COMMAND + "=" + GetVariables.GETVERSION);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new ArcImsException("arcims_no_server");
        }
        try {
            conn = (HttpURLConnection) urlCmd.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.connect();
            isr = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String strTemp = new String();
            strTemp = reader.readLine();
            getV[0] = strTemp.substring(strTemp.indexOf("=") + 1);
            strTemp = reader.readLine();
            getV[1] = strTemp.substring(strTemp.indexOf("=") + 1);
            conn.disconnect();
        } catch (ConnectException ce) {
            logger.error("Timed out error", ce);
            throw new ArcImsException("arcims_server_timeout");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ArcImsException("arcims_no_server");
        }
        return getV;
    }
