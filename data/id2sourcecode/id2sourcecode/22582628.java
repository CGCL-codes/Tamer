    private boolean downloadFile() {
        URL url;
        HttpURLConnection huc;
        byte[] buffer = new byte[4096];
        int totBytes, bytes, sumBytes = 0;
        try {
            url = new URL(srcFileURL);
            huc = (HttpURLConnection) url.openConnection();
            huc.connect();
            InputStream is = huc.getInputStream();
            int code = huc.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                File output = new File(destFileLocale);
                FileOutputStream outputStream = new FileOutputStream(output);
                totBytes = huc.getContentLength();
                while ((bytes = is.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, bytes);
                    sumBytes += bytes;
                    downloadProgress = (Math.round((((float) sumBytes / (float) totBytes) * 100)));
                }
                huc.disconnect();
                return true;
            }
            huc.disconnect();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
