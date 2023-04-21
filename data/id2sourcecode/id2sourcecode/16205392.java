    public byte[] getHtmlBytes(String strUrl, String strPost, boolean isPost, String encoding) {
        byte[] ret = null;
        HttpURLConnection httpCon = null;
        InputStream is = null;
        try {
            URL url = new URL(strUrl);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setReadTimeout(timeout);
            httpCon.setConnectTimeout(timeout);
            httpCon.setUseCaches(false);
            httpCon.setInstanceFollowRedirects(true);
            httpCon.setRequestProperty("Referer", referer);
            httpCon.setRequestProperty("Content-Type", CONTENT_TYPE);
            httpCon.setRequestProperty("Accept", ACCEPT);
            httpCon.setRequestProperty("User-Agent", USER_AGENT);
            httpCon.setRequestProperty("Cookie", cookies.toString());
            if (isPost) {
                httpCon.setDoOutput(true);
                httpCon.setRequestMethod("POST");
                httpCon.connect();
                OutputStream os = null;
                try {
                    os = httpCon.getOutputStream();
                    os.write(URLEncoder.encode(strPost, encoding).getBytes());
                    os.flush();
                } finally {
                    if (os != null) os.close();
                }
            }
            is = httpCon.getInputStream();
            ByteArrayBuffer baBuffer = null;
            byte[] buffer = new byte[BUFFER_LENGTH];
            int rNum = 0;
            baBuffer = new ByteArrayBuffer(BUFFER_LENGTH << 1);
            while ((rNum = is.read(buffer)) != -1) {
                baBuffer.append(buffer, 0, rNum);
            }
            ret = baBuffer.toByteArray();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + ":" + e.getCause());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (httpCon != null) {
                cookies.putCookies(httpCon.getHeaderField("Set-Cookie"));
                referer = strUrl;
                httpCon.disconnect();
            }
        }
        return ret;
    }
