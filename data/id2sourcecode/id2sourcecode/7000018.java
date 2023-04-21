    public static boolean downloadImage(String imageUrl, String filename) {
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        boolean deleteFile = false;
        try {
            logger.debug("Attempting to download image from url " + imageUrl);
            URL url = new URL(imageUrl);
            out = new BufferedOutputStream(new FileOutputStream(filename));
            conn = url.openConnection();
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
            }
            logger.debug("Downloaded image to file " + filename);
        } catch (Exception e) {
            deleteFile = true;
            logger.error("Failed to download image " + imageUrl, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                ;
            }
            if (deleteFile) {
                logger.debug("Failed downloading image - removing file");
                File f = new File(filename);
                if (f.exists()) {
                    f.delete();
                    logger.debug("Image file " + filename + " deleted");
                }
            }
        }
        return !deleteFile;
    }
