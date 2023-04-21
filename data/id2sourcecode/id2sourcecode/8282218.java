    public static boolean downloadSubtitleFromGoogle(int index) {
        System.out.println("Download subtitle " + index);
        RemoteSource rs = remoteResources.get(0);
        String sitePath = rs.getSubtitleFileAddress(index);
        int respCode = -1;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(sitePath);
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            respCode = response.getStatusLine().getStatusCode();
            if (respCode != 200) {
                System.out.println("CheckRemoteThread: check " + sitePath + " return " + respCode);
                return false;
            }
            InputStream is = response.getEntity().getContent();
            FileOutputStream fos = new FileOutputStream(FileSysManager.getLocalSubtitleFile(index));
            byte[] buf = new byte[16384];
            int readLen = -1;
            int counter = 0;
            Log.d(logTag, "Start read stream from remote site, is=" + ((is == null) ? "NULL" : "exist") + ", buf=" + ((buf == null) ? "NULL" : "exist"));
            while ((readLen = is.read(buf)) != -1) {
                counter += readLen;
                fos.write(buf, 0, readLen);
            }
            is.close();
            fos.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
