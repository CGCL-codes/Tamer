    public void run() {
        boolean failed = false;
        boolean wasCached = false;
        String failureReason = null;
        livingThreads++;
        String postfix = getId();
        File file = new File(postfix + "html");
        cat.debug("I am " + Thread.currentThread());
        cat.debug("Trying to retrieve url " + prefix + postfix);
        cat.debug("saving in File: " + file.toString());
        OutputStream out = null;
        startDate = new Date();
        if (!failed) {
            try {
                long lastModified = file.lastModified();
                long fileAge = (new Date()).getTime() - lastModified;
                if (lastModified == 0 || (fileAge > cacheTimeOut) || (file.length() == 0)) {
                    out = new FileOutputStream(file);
                } else {
                    wasCached = true;
                }
            } catch (FileNotFoundException exc) {
                failed = true;
                failureReason = REASON_NO_OUTPUT;
            }
        }
        if (!wasCached) {
            try {
                Thread.currentThread().sleep(delay);
            } catch (InterruptedException exc) {
                exc.printStackTrace(System.err);
            }
            URL url = null;
            try {
                url = new URL(prefix + postfix);
            } catch (MalformedURLException exc) {
                failed = true;
                failureReason = REASON_MALFORMED_URL;
            }
            InputStream in = null;
            if (!failed) {
                URLConnection con = null;
                try {
                    con = url.openConnection();
                    in = con.getInputStream();
                } catch (IOException exc) {
                    failed = true;
                    failureReason = REASON_DOWNLOAD_INI_FAILED;
                }
            }
            String content = "";
            if (!failed) {
                try {
                    byte[] tmp = new byte[50000];
                    int length = 0;
                    while ((length = in.read(tmp)) > -1) {
                        content += new String(tmp, 0, length);
                        cat.debug(myId + ": " + content.length() + " bytes read");
                    }
                } catch (IOException exc) {
                    failed = true;
                    failureReason = REASON_DOWNLOAD_FAILED;
                }
            }
            if (!failed) {
                try {
                    out.write(content.getBytes());
                    out.flush();
                    out.close();
                } catch (IOException exc) {
                    failed = true;
                } finally {
                    cat.info("finished " + Thread.currentThread());
                }
            }
            if (failed) {
                failedDownloads++;
                int failures = (((Integer) failureStatistics.get(failureReason)).intValue());
                failures++;
                failureStatistics.put(failureReason, new Integer(failures));
            } else {
                succeededDownloads++;
            }
        }
        if (wasCached) {
            cachedPages++;
        }
        livingThreads--;
    }
