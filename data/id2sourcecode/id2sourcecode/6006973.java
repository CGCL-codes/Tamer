    public static void downloadFile(String sourceurl, String dest) {
        URL url;
        try {
            url = new URL(sourceurl);
            HttpURLConnection hConnection = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            if (HttpURLConnection.HTTP_OK == hConnection.getResponseCode()) {
                InputStream in = hConnection.getInputStream();
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
                int filesize = hConnection.getContentLength();
                byte[] buffer = new byte[4096];
                int numRead;
                long numWritten = 0;
                while ((numRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, numRead);
                    numWritten += numRead;
                    System.out.println((double) numWritten / (double) filesize);
                }
                if (filesize != numWritten) System.out.println("Wrote " + numWritten + " bytes, should have been " + filesize); else System.out.println("Downloaded successfully.");
                out.close();
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
