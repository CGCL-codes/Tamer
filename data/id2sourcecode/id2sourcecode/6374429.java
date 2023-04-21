    public InputStream getTagsAsInputStream(String content) {
        OutputStreamWriter osw = null;
        InputStream is = null;
        try {
            String reqData = URLEncoder.encode(paramName, "UTF-8") + "=" + URLEncoder.encode(content, "UTF-8");
            URL service = new URL(cmdUrl);
            URLConnection urlConn = service.openConnection();
            urlConn.setDoOutput(true);
            urlConn.connect();
            osw = new OutputStreamWriter(urlConn.getOutputStream());
            osw.write(reqData);
            osw.flush();
            is = urlConn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return is;
    }
