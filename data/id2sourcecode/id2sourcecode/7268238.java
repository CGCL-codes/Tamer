    private void download(String address) throws Exception {
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        try {
            URL url = new URL(address);
            conn = url.openConnection();
            String localFileName = getFileName(address, conn.getContentType());
            out = new BufferedOutputStream(new FileOutputStream(localFileName));
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
            }
        } catch (MalformedURLException mue) {
            logger.error("Malformed URL " + address, mue);
            throw new Exception("Malformed URL " + address);
        } catch (IOException ioe) {
            logger.error("IO Exception occured while downloading " + address, ioe);
            throw new Exception("IO error occured while downloading " + address);
        } catch (Exception e) {
            logger.error("Exception occured while downloading " + address, e);
            throw new Exception("Error occured while downloading " + address);
        } finally {
            try {
                if (null != in) in.close();
                if (null != out) out.close();
            } catch (IOException ioe) {
                logger.error("Failed to close the input/output stream " + ioe.getMessage(), ioe);
            }
        }
    }
