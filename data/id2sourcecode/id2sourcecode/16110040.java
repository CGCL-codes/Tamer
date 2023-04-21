    public boolean tryConnectAndSendStopCommand(int port) {
        InputStream is = null;
        HttpURLConnection urlcon = null;
        try {
            URL stopUrl = new URL("http://127.0.0.1:" + port + "/sdloader-command/stop");
            urlcon = (HttpURLConnection) stopUrl.openConnection();
            urlcon.setRequestMethod("POST");
            urlcon.setUseCaches(false);
            urlcon.setConnectTimeout(1000);
            urlcon.setReadTimeout(1000);
            urlcon.setDoInput(true);
            urlcon.setDoOutput(true);
            int responseCode = urlcon.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        } finally {
            IOUtil.closeNoException(is);
            IOUtil.closeHttpUrlConnectionNoException(urlcon);
        }
    }
