    private void netInit() {
        try {
            URL url = new URL(this.initUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.connect();
            InputStream in = conn.getInputStream();
            Document rtnDoc = Dom4jUtil.getDocFromStream(in);
            processInitInfor(rtnDoc);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
