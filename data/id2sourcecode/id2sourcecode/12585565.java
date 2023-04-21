    public static void downloadFileToDisk(String address, String filename) throws MalformedURLException {
        URLConnection urlConn = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            URL url = new URL(address);
            urlConn = url.openConnection();
            in = urlConn.getInputStream();
            out = new BufferedOutputStream(new FileOutputStream(filename));
            byte[] byteBuffer = new byte[1024];
            int numRead;
            while ((numRead = in.read(byteBuffer)) != -1) {
                out.write(byteBuffer, 0, numRead);
            }
        } catch (IOException ex) {
            Log.logErrorRB("IO exception");
            Log.log(ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
            }
        }
    }
