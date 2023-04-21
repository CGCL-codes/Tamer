    public String postUrl(String urlStr, String sendData) throws IOException {
        URL url;
        String htmlData = "";
        url = new URL(urlStr);
        String content = sendData;
        URLConnection uc = url.openConnection();
        uc.setDoOutput(true);
        uc.setUseCaches(false);
        uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        uc.setRequestProperty("Content-Length", "" + content.length());
        HttpURLConnection hc = (HttpURLConnection) uc;
        hc.setRequestMethod("POST");
        OutputStream os = uc.getOutputStream();
        os.write(content.getBytes());
        os.close();
        InputStream is = uc.getInputStream();
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(is, HTTP.ISO_8859_1));
        StringBuffer sb = new StringBuffer();
        String tempStr;
        while ((tempStr = bufReader.readLine()) != null) {
            sb.append(tempStr).append("\r\n");
        }
        is.close();
        bufReader.close();
        htmlData = new String(sb.toString().getBytes(HTTP.ISO_8859_1), "utf-8");
        return htmlData;
    }
