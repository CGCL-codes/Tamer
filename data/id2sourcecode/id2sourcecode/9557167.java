    public void download(String address, String localFileName) {
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        try {
            URL url = new URL(address);
            out = new BufferedOutputStream(new FileOutputStream(localFileName));
            conn = url.openConnection();
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
            }
            logger.info(localFileName + "\t" + numWritten);
        } catch (Exception e1) {
            logger.severe("Exception ocurred while reading file");
            logger.severe("Filename is: " + localFileName);
            logger.severe("Exception is: " + e1.getMessage());
            try {
                out.close();
                File f = new File(localFileName);
                if (f.exists()) {
                    f.delete();
                }
            } catch (Exception e2) {
                logger.severe("Exception ocurred while closing file after reading exception");
                logger.severe("Filename is: " + localFileName);
                logger.severe("Exception is: " + e1.getMessage());
            }
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
