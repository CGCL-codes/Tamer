    public void run() {
        if (url == null) {
            stateType = DownStateType.ERROR;
            return;
        }
        while (startPos < endPos && !bStop) {
            if (stateType == DownStateType.STATE_STOPPED) {
                splitterStop();
                return;
            }
            while (stateType == DownStateType.PAUSE) {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stateType = DownStateType.DOWNLOADING;
            try {
                HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.00; Windows 98)");
                uc.setRequestProperty("JCache-Controlt", "no-cache");
                String sProperty = "bytes=" + startPos + "-";
                uc.setRequestProperty("RANGE", sProperty);
                if (uc instanceof HttpsURLConnection) {
                    HttpsURLConnection httpsConnection = (HttpsURLConnection) uc;
                    httpsConnection.setHostnameVerifier(new HostnameVerifier() {

                        public boolean verify(String host, SSLSession session) {
                            return true;
                        }
                    });
                }
                InputStream input = uc.getInputStream();
                byte[] b = new byte[1024];
                int nRead;
                while ((nRead = input.read(b, 0, 1024)) > 0 && startPos < endPos && !bStop) {
                    startPos += fileAccess.write(b, 0, nRead);
                    completed = completed + nRead;
                    stateType = DownStateType.DOWNLOADING;
                }
                log.info("Thread " + nThreadID + " is over!");
                bDownOver = true;
                stateType = DownStateType.FINISH;
                uc.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                log.info(e);
                stateType = DownStateType.ERROR;
            }
        }
        splitterStop();
    }
