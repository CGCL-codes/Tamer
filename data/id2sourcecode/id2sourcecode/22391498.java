    public byte[] sendPost(String url, byte[] param) throws Exception {
        HttpURLConnection huc = null;
        InputStream is = null;
        OutputStream outputStream = null;
        try {
            URL httpurl = new URL(url);
            huc = (HttpURLConnection) httpurl.openConnection();
            huc.setDoOutput(true);
            huc.setConnectTimeout(10000);
            huc.setReadTimeout(10000);
            huc.setRequestProperty("Content-length", "" + param.length);
            outputStream = huc.getOutputStream();
            outputStream.write(param);
            outputStream.flush();
            is = huc.getInputStream();
            byte[] resultBytes = new byte[huc.getContentLength()];
            byte[] tempByte = new byte[1024];
            int length = 0;
            int index = 0;
            while ((length = is.read(tempByte)) != -1) {
                System.arraycopy(tempByte, 0, resultBytes, index, length);
                index += length;
            }
            return resultBytes;
        } catch (Exception e) {
            throw new Exception();
        } finally {
            is.close();
            huc.disconnect();
            outputStream.close();
        }
    }
