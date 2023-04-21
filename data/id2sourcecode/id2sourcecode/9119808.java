    public File download(String address, String localFileName) {
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        boolean succeeded = false;
        try {
            logger.info("Trying to retrieve " + address);
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
            succeeded = true;
        } catch (Exception e1) {
            logger.severe("Exception ocurred while reading file: " + localFileName);
            try {
                out.close();
                File f = new File(localFileName);
                if (f.exists()) {
                    f.delete();
                }
            } catch (Exception e2) {
                logger.severe("Exception ocurred while closing file after reading exception: " + localFileName);
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
        if (succeeded) return new File(localFileName); else return null;
    }
