    public void testAccess() {
        try {
            String data = "VER 1 MSNP14 MSNP13 CVR0\r\n";
            data = URLEncoder.encode(data, "UTF-8");
            String urlStr = "http://gateway.messenger.hotmail.com/gateway/gateway.dll?" + URLEncoder.encode("Action=open&Server=NS&IP=messenger.hotmail.com HTTP/1.1", "utf-8");
            URL url = new URL(urlStr);
            HttpURLConnection msnCon = (HttpURLConnection) url.openConnection();
            msnCon.setFollowRedirects(false);
            msnCon.setDoOutput(true);
            msnCon.setDoInput(true);
            msnCon.setRequestProperty("Accept", "*/*");
            msnCon.setRequestProperty("Accept-Language", "en-us");
            msnCon.setRequestProperty("Accept-Encoding", "gzip, deflate");
            msnCon.setRequestProperty("User-Agent", "MSMSGS");
            msnCon.setRequestProperty("Host", "gateway.messenger.hotmail.com");
            msnCon.setRequestProperty("Proxy-Connection", "Keep-Alive");
            msnCon.setRequestProperty("Connection", "Keep-Alive");
            msnCon.setRequestProperty("Pragma", "no-cache");
            msnCon.setRequestProperty("Content-Type", "application/x-msn-messenger");
            msnCon.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
            OutputStream out = msnCon.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            printHeaders(msnCon);
            PosterOutputStream outputStream = (PosterOutputStream) out;
            outputStream.write("xxx".getBytes());
            printHeaders(msnCon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
