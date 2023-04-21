    public void startDownload() {
        steps = 0;
        try {
            Method m = DownloadListener.class.getMethod("downloadStarted", DownloadProcess.class);
            downloadListeners.callMethod(m, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(urlString);
            is = url.openStream();
            fos = new FileOutputStream(outFile);
            int oneChar, counter = 0;
            while ((oneChar = is.read()) != -1) {
                fos.write(oneChar);
                counter++;
                if (counter == STEP_FACTOR) {
                    steps++;
                    counter = 0;
                    try {
                        Method m = DownloadListener.class.getMethod("downloadStatusChanged", DownloadProcess.class);
                        downloadListeners.callMethod(m, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Method m = DownloadListener.class.getMethod("downloadFinished", DownloadProcess.class);
                downloadListeners.callMethod(m, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
