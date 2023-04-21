    public boolean writeToStream(OutputStream os) {
        BufferedInputStream is = null;
        boolean success = false;
        try {
            logger.debug("Playing:" + file + " from " + this.startPoint);
            is = new BufferedInputStream(new FileInputStream(file));
            if (this.startPoint != 0) is.skip(this.startPoint);
            byte input[] = new byte[4096];
            int bytesread;
            long totalbytes = 0;
            while ((bytesread = is.read(input)) != -1) {
                os.write(input, 0, bytesread);
                totalbytes += bytesread;
            }
            os.flush();
            logger.debug("End of File Reached, wrote:" + totalbytes);
            logger.debug("End of File Reached, wrote:" + is.read());
            success = true;
        } catch (Exception e) {
            logger.debug(e.toString());
        } finally {
            if (is != null) try {
                is.close();
            } catch (Exception e) {
            }
        }
        return success;
    }
