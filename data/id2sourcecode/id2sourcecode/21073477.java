    public static String getHtml(DefaultHttpClient httpclient, String url, String encode) throws Exception {
        InputStream input = null;
        try {
            HttpGet get = new HttpGet(url);
            HttpResponse res = httpclient.execute(get);
            StatusLine status = res.getStatusLine();
            if (status.getStatusCode() != 200) {
                throw new RuntimeException("50001");
            }
            if (res.getEntity() == null) {
                return "";
            }
            input = res.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(input, encode);
            BufferedReader bufReader = new BufferedReader(reader);
            String tmp = null, html = "";
            while ((tmp = bufReader.readLine()) != null) {
                html += tmp;
            }
            return html;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("50002");
        } finally {
            if (input != null) input.close();
        }
    }
