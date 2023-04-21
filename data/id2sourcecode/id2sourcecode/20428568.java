    public static final void connectExecuteDisconnect(String url, SillyRawLevelMultiPartRequest process) {
        URL urlObj = null;
        HttpURLConnection urlCon = null;
        try {
            urlObj = new URL(url);
            urlCon = (HttpURLConnection) urlObj.openConnection();
            process.execute(urlCon);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlCon != null) {
                urlCon.disconnect();
            }
        }
    }
