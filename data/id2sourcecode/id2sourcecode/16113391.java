    public String get(String urlAsText) {
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        try {
            String localFilename = nextFilename(urlAsText);
            URL url = new URL(urlAsText);
            out = new BufferedOutputStream(new FileOutputStream(localFilename));
            conn = url.openConnection();
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
            }
            _downloaded.add(localFilename);
            return localFilename;
        } catch (Exception exception) {
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
            }
        }
    }
