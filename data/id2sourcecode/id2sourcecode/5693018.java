    private void openNextStreamFile() {
        log.info("opening next data file from URL stream");
        String fileName = getNextFileName();
        if (fileName == null) {
            return;
        }
        try {
            log.info("opening data file url input stream from " + fileName);
            URL url = new URL(fileName);
            InputStream is = new BufferedInputStream(url.openStream());
            aeRecordedInputStream = new AEInputStream(is);
            stopflag = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
