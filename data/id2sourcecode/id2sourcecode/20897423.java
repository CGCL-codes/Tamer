    public boolean urlDownloadToFile(Context context, String strurl, String path) {
        boolean bRet = false;
        detectProxy();
        try {
            URL url = new URL(strurl);
            HttpURLConnection conn = null;
            if (mProxy != null) {
                conn = (HttpURLConnection) url.openConnection(mProxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            bRet = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bRet;
    }
