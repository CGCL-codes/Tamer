    public ComponentIdentifier install(String location) throws IllegalComponentStateException {
        logger.debug("Install from location=" + location);
        File localFile = null;
        try {
            URL url = new URL(location);
            logger.debug("Got URL=" + url);
            localFile = new File(frameworkRoot, url.getFile());
            int read = 0;
            InputStream in = null;
            OutputStream out = null;
            try {
                logger.debug("Writing to local file=" + localFile.getAbsolutePath());
                in = url.openStream();
                logger.debug("Got input stream");
                out = new FileOutputStream(localFile);
                logger.debug("Got output stream");
                boolean reading = true;
                byte[] buffer = new byte[1024];
                int len = 10;
                while (len > 0) {
                    len = in.read(buffer);
                    if (len > 0) {
                        out.write(buffer, 0, len);
                        read += len;
                        logger.debug("Transferred block size=" + len + " bytes");
                    } else {
                        logger.debug("EOF encountered after " + read + " bytes; stop transfer");
                        reading = false;
                    }
                }
            } catch (IOException ioe) {
                logger.error("General IO problem after " + read + " bytes", ioe);
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                    logger.debug("LocalController and remote files transferred " + read + " bytes - complete");
                } catch (IOException e1) {
                    logger.error("Unable to close streams after transfer", e1);
                }
            }
        } catch (MalformedURLException e) {
            logger.debug("Not a real URL=" + location + "; trying as local file...");
            localFile = new File(location);
        }
        return install(localFile, location);
    }
