    public void uploadZip() {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        FileOutputStream fos = null;
        int responseCode = 0;
        byte[] data = null;
        File dirs = null;
        File file = null;
        if (isRun) {
            try {
                sendMsg(progressValue);
                Thread.sleep(10);
                while (progressValue < 10) {
                    progressValue++;
                    sendMsg(progressValue);
                }
                url = new URL(downloadUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(CONNECT_TIME);
                responseCode = conn.getResponseCode();
                is = conn.getInputStream();
                while (progressValue < 20) {
                    progressValue++;
                    sendMsg(progressValue);
                }
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    data = HttpUtil.getByte(is);
                }
                while (progressValue < 25) {
                    progressValue++;
                    sendMsg(progressValue);
                }
                dirs = new File(UrlConfigUtil.APP_URL + File.separator + app.getEnName());
                while (progressValue < 30) {
                    progressValue++;
                    sendMsg(progressValue);
                }
                if (!dirs.exists()) {
                    dirs.mkdirs();
                }
                while (progressValue < 40) {
                    progressValue++;
                    sendMsg(progressValue);
                }
                file = new File(dirs, fileName);
                if (!file.exists()) {
                    boolean createNewFile = file.createNewFile();
                    if (createNewFile) {
                        Log.d("createNewFile", String.valueOf(createNewFile));
                    } else {
                        Log.d("createNewFile", String.valueOf(createNewFile));
                    }
                }
                while (progressValue < 50) {
                    progressValue++;
                    sendMsg(progressValue);
                }
                fos = new FileOutputStream(file);
                fos.write(data);
                while (progressValue < 60) {
                    progressValue++;
                    sendMsg(progressValue);
                }
                if (fos != null) {
                    try {
                        fos.flush();
                        fos.close();
                        fos = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                while (progressValue < 70) {
                    progressValue++;
                    sendMsg(progressValue);
                }
                if (is != null) {
                    try {
                        is.close();
                        is = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                while (progressValue < 80) {
                    progressValue++;
                    sendMsg(progressValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
