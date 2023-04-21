    public void GetHtml(String url, File output) throws IOException {
        GetMethod method = new GetMethod(url);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        int statusCode = client.executeMethod(method);
        if (statusCode == HttpStatus.SC_OK) {
            String encode = method.getResponseCharSet();
            InputStream is = method.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encode));
            OutputStreamWriter out = null;
            try {
                out = new OutputStreamWriter(new FileOutputStream(output), encode);
                String line = null;
                while ((line = br.readLine()) != null) out.write(line);
            } finally {
                if (out != null) try {
                    out.close();
                } catch (Exception e1) {
                }
                if (br != null) try {
                    br.close();
                } catch (Exception e1) {
                }
            }
        }
    }
