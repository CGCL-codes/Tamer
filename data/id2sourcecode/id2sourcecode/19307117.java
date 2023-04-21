    public static byte[] sendPost(String url, byte[] param) {
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
            byte[] resultBytes = new byte[httpURLConnection.getContentLength()];
            byte[] tempByte = new byte[1024];
            int length = 0;
            int index = 0;
            while ((length = is.read(tempByte)) != -1) {
                System.arraycopy(tempByte, 0, resultBytes, index, length);
                index += length;
            }
            is.close();
            return resultBytes;
        } catch (Exception e) {
            e.printStackTrace();
            MsgPrint.showMsg("没有结果！" + e);
        }
        return null;
    }
