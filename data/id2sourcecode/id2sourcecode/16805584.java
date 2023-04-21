    public String connectURL(String encode, String dest_url, String commString, String content_type, String content_lan) {
        String rec_string = "";
        URL url = null;
        HttpURLConnection urlconn = null;
        urlconn = null;
        OutputStream out = null;
        BufferedReader rd = null;
        StringBuffer sb = null;
        try {
            dest_url = dest_url.replace(" ", "%20");
            url = new URL(dest_url);
            urlconn = (HttpURLConnection) url.openConnection();
            urlconn.setConnectTimeout(30000);
            urlconn.setReadTimeout(30000);
            if (urlconn != null) {
                urlconn.setRequestProperty("content-type", content_type);
                urlconn.setRequestProperty("User-Agent", "Khan HttpConnection request");
                urlconn.setRequestProperty("Accept", "*.*");
                urlconn.setRequestProperty("Content-Language", content_lan);
                urlconn.setRequestProperty("Catche-Control", "no-cache");
                urlconn.setRequestMethod("POST");
                urlconn.setDoInput(true);
                urlconn.setDoOutput(true);
                out = urlconn.getOutputStream();
                out.write(commString.getBytes(encode));
                out.flush();
                out.close();
                rd = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
                sb = new StringBuffer();
                int ch;
                while ((ch = rd.read()) > -1) {
                    sb.append((char) ch);
                }
                rec_string = sb.toString();
                rd.close();
            }
        } catch (Exception e) {
            Common.sleep(200);
        } finally {
            if (urlconn != null) {
                urlconn.disconnect();
                urlconn = null;
            }
            url = null;
            out = null;
            rd = null;
            sb = null;
        }
        return rec_string;
    }
