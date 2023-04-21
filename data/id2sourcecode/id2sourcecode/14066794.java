    public static void downFile(String urL, String path, DownloadInfoReceiver dlr, AtomicBoolean cancelFlag) {
        DownloadInfo downInfo = new DownloadInfo();
        try {
            logger.log("downloading file from:" + urL + " save to:" + path);
            URL uRL = new URL(urL);
            URLConnection conn = uRL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            downInfo.fileSize = conn.getContentLength();
            if (downInfo.fileSize <= 0) {
                logger.log("unknown content length");
                downInfo.fileSize = DownloadInfo.UNKNOWN_CONTENT_LENGTH;
            }
            dlr.startReceive(downInfo);
            if (is == null) throw new RuntimeException("stream is null");
            File file = new File(path);
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            downInfo.downloadedBytes = 0;
            do {
                int numread = is.read(buf);
                if (numread == -1) {
                    break;
                }
                fos.write(buf, 0, numread);
                downInfo.downloadedBytes += numread;
                dlr.newBytesSaved(downInfo);
            } while (!cancelFlag.get());
            fos.close();
            if (!cancelFlag.get()) {
                downInfo.filePath = path;
                dlr.complete(downInfo);
                logger.log("finish downloading file from:" + urL + " save to:" + path);
            } else {
                boolean deleted = file.delete();
                dlr.canceled(downInfo);
                logger.log("download canceld by user,file " + path + (deleted ? " deleted" : " not deleted"));
            }
            is.close();
        } catch (Exception e) {
            logger.log("error: " + e.getMessage(), e);
            downInfo.errorMsg = e.getMessage();
            dlr.errorOccur(downInfo);
        }
    }
