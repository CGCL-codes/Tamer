    public File download() {
        try {
            URL url = new URL(file);
            FileOutputStream os = new FileOutputStream(f);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            int i = 0;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                byte tmp_buffer[] = new byte[4096];
                InputStream is = conn.getInputStream();
                int n;
                while ((n = is.read(tmp_buffer)) > 0) {
                    i++;
                    os.write(tmp_buffer, 0, n);
                    os.flush();
                    int val = (int) ((double) (i * 2860) / (double) conn.getContentLength() * 100);
                    if (recv != null && val < 100) recv.setProgress(val);
                }
                if (recv != null) recv.setProgress(104);
            }
            return f;
        } catch (Exception exc) {
            if (Constants.debug) exc.printStackTrace();
            return null;
        }
    }
