    public void run() {
        try {
            LOG.info("thread started");
            OutputStream os = url.openConnection().getOutputStream();
            InputStream is = new FileInputStream(file);
            byte[] buf = new byte[4096];
            int len;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
            is.close();
            os.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            LOG.error(ex);
            exception = ex;
        } finally {
            LOG.info("thread stopped");
        }
    }
