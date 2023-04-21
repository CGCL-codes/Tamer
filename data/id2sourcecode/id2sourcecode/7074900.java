    public byte[] sendPost(String url, byte[] param) {
        try {
            URL httpurl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) httpurl.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-length", "" + param.length);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(param);
            outputStream.flush();
            outputStream.close();
            InputStream is = httpURLConnection.getInputStream();
            byte[] bytes = new byte[httpURLConnection.getContentLength()];
            is.read(bytes, 0, httpURLConnection.getContentLength());
            is.close();
            return bytes;
        } catch (Exception e) {
        }
        return null;
    }
