    private void download(URL url, CacheListener listener, CachePolicy policy) throws IOException {
        long start = System.currentTimeMillis();
        logger.info("Downloading Resource: " + url.toString());
        String name = getDownloadName(url);
        CacheEvent event = new CacheEvent(this, name, url, -1, -1, -1, -1);
        if (listener != null) {
            listener.startDownload(event);
        }
        if (policy == null) {
            policy = CachePolicy.ALWAYS;
        }
        File file = null;
        try {
            URLConnection conn = url.openConnection();
            logger.finer("Getting Download Stream: " + url.toString());
            InputStream in = conn.getInputStream();
            long lastModified = conn.getLastModified();
            int size = conn.getContentLength();
            logger.finer("Done Getting Download Stream: " + url.toString());
            if (!calculate_progress) size = -1;
            int progressSize = 0;
            long progressTime = 0;
            long estimateTime = 0;
            long startTime = System.currentTimeMillis();
            file = getCachedFileForURL(url);
            FileOutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[BUF_SIZE];
            int count = in.read(buf);
            while (count > 0) {
                out.write(buf, 0, count);
                progressSize += count;
                progressTime = System.currentTimeMillis() - startTime;
                if (size != -1 && progressSize < size) {
                    estimateTime = Math.round(((1.0 / (progressSize / (double) size)) * (double) progressTime) - progressTime);
                } else {
                    estimateTime = -1;
                }
                event = new CacheEvent(this, name, url, size, progressSize, progressTime, estimateTime);
                if (listener != null) {
                    listener.updateDownload(event);
                }
                if (DELAY > 0) {
                    try {
                        Thread.sleep(DELAY);
                    } catch (Exception e) {
                        logger.severe(e.getMessage());
                    }
                }
                count = in.read(buf);
            }
            in.close();
            out.close();
            recentDownloads.put(url, new Long(System.currentTimeMillis()));
            writeInfo(url, file, policy, true);
            if (lastModified != 0) {
                file.setLastModified(lastModified);
            }
            estimateTime = 0;
            event = new CacheEvent(this, name, url, size, progressSize, progressTime, estimateTime);
            if (listener != null) {
                listener.completeDownload(event);
            }
            logger.info("Downloading Complete:" + url.toString());
            logger.fine("Download Time:" + (System.currentTimeMillis() - start));
        } catch (IOException ioExp) {
            logger.log(Level.WARNING, ioExp.getMessage(), ioExp);
            writeInfo(url, file, policy, false);
            if (listener != null) {
                listener.downloadException(name, url, ioExp.getMessage(), ioExp);
            }
            throw ioExp;
        }
    }
