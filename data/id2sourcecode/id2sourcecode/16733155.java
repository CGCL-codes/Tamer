    private void processFetch(String[] fetchBuf2, int numInBuf2, OutputStream os) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < numInBuf2; i++) {
            if (i > 0) buf.append("|");
            buf.append("recordnumber:").append(fetchBuf2[i]);
        }
        String fullUrlStr = urlBase + buf.toString();
        InputStream in = null;
        try {
            URL url = new URL(fullUrlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.connect();
            in = httpConn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            int read = 0;
            int bufSize = 512;
            byte[] buffer = new byte[bufSize];
            while (true) {
                read = bis.read(buffer);
                if (read == -1) {
                    break;
                }
                os.write(buffer, 0, read);
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }
