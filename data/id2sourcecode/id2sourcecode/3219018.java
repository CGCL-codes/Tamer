    public void persist(Content content) {
        Link link = content.getLink();
        byte[] buffer = content.getBuffer();
        if (buffer != null) {
            File rootDir;
            if (getParameter("rootDir") != null) {
                rootDir = new File(getParameter("rootDir"));
            } else {
                rootDir = new File(".");
            }
            if (!rootDir.exists()) {
                rootDir.mkdirs();
            }
            File file = linkToFilePath(link, rootDir, content.getContentType());
            if (file == null) {
                log.error("persist(): Unable to convert url " + link + " to a correct file name");
                logPers.error("Unable to convert url " + link + " to a correct file name");
                return;
            }
            log.debug("persist(): File is " + file.getAbsolutePath());
            if (file.exists() && file.length() == buffer.length) {
                log.info("persist(): File " + file.getAbsolutePath() + " exists");
            } else {
                try {
                    log.debug("persist(): Allocating buffer of size " + buffer.length);
                    ByteBuffer bbuf = ByteBuffer.allocate(buffer.length);
                    bbuf.put(buffer);
                    bbuf.flip();
                    WritableByteChannel wChannel = new FileOutputStream(file).getChannel();
                    log.debug("persist(): Got channel for file " + file.getAbsolutePath());
                    int numWritten = wChannel.write(bbuf);
                    log.debug("persist(): Wrote " + numWritten + " bytes on channel for file " + file.getAbsolutePath());
                    wChannel.close();
                    log.debug("persist(): Closed channel for file " + file.getAbsolutePath());
                    bbuf.flip();
                    bbuf.clear();
                    logPers.info("The buffer for url " + link + " was successfully " + "saved on file " + file.getAbsolutePath());
                } catch (IOException e) {
                    log.error("persist(): Problem saving buffer for file " + file.getAbsolutePath(), e);
                    logPers.error("Error saving buffer for file " + file.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        } else {
            log.warn("persist(): the buffer for url " + link + " is NULL");
            logPers.warn("The buffer for url " + link + " is NULL");
        }
    }
