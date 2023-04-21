    protected void downloadJars(String path) throws Exception {
        setState(STATE_DOWNLOADING);
        URLConnection urlconnection;
        int initialPercentage = percentage = 15;
        byte buffer[] = new byte[65536];
        for (int i = 0; i < urlList.length; i++) {
            if (fileSizes[i] == -2) continue;
            int unsuccessfulAttempts = 0;
            int maxUnsuccessfulAttempts = 3;
            boolean downloadFile = true;
            while (downloadFile) {
                downloadFile = false;
                debug_sleep(2000);
                urlconnection = urlList[i].openConnection();
                if (urlconnection instanceof HttpURLConnection) {
                    urlconnection.setRequestProperty("Cache-Control", "no-cache");
                    urlconnection.connect();
                }
                String currentFile = getFileName(urlList[i]);
                InputStream inputstream = getJarInputStream(currentFile, urlconnection);
                FileOutputStream fos = new FileOutputStream(path + currentFile);
                int bufferSize;
                long downloadStartTime = System.currentTimeMillis();
                int downloadedAmount = 0;
                int fileSize = 0;
                String downloadSpeedMessage = "";
                while ((bufferSize = inputstream.read(buffer, 0, buffer.length)) != -1) {
                    debug_sleep(10);
                    fos.write(buffer, 0, bufferSize);
                    currentSizeDownload += bufferSize;
                    fileSize += bufferSize;
                    percentage = initialPercentage + ((currentSizeDownload * 45) / totalSizeDownload);
                    subtaskMessage = "Retrieving: " + currentFile + " " + ((currentSizeDownload * 100) / totalSizeDownload) + "%";
                    downloadedAmount += bufferSize;
                    long timeLapse = System.currentTimeMillis() - downloadStartTime;
                    if (timeLapse >= 1000) {
                        float downloadSpeed = (float) downloadedAmount / timeLapse;
                        downloadSpeed = ((int) (downloadSpeed * 100)) / 100f;
                        downloadSpeedMessage = " - " + downloadSpeed + " KB/sec";
                        downloadedAmount = 0;
                        downloadStartTime = System.currentTimeMillis();
                    }
                    subtaskMessage += downloadSpeedMessage;
                }
                inputstream.close();
                fos.close();
                if (urlconnection instanceof HttpURLConnection) {
                    if (fileSize == fileSizes[i]) {
                    } else if (fileSizes[i] <= 0) {
                    } else {
                        unsuccessfulAttempts++;
                        if (unsuccessfulAttempts < maxUnsuccessfulAttempts) {
                            downloadFile = true;
                            currentSizeDownload -= fileSize;
                        } else {
                            throw new Exception("failed to download " + currentFile);
                        }
                    }
                }
            }
        }
        subtaskMessage = "";
    }
