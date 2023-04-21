    public static String doPost(String URL, Map<String, String> params) {
        OutputStream os = null;
        BufferedReader br = null;
        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(SERVLET_POST);
            String paramStr = prepareParam(params);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            os = conn.getOutputStream();
            os.write(paramStr.toString().getBytes("utf-8"));
            os.close();
            String charset = getChareset(conn.getContentType());
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            String line;
            StringBuffer result = new StringBuffer();
            while ((line = br.readLine()) != null) {
                result.append(line + "\n");
            }
            return result.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
