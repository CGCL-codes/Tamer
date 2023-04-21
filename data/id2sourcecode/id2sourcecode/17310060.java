    private Downloader(final Map<URL, File> urlFileMap, final int timeout) {
        thread = new Thread(new Runnable() {

            public void run() {
                incrementTotalActiveDownloadsCount();
                long startTime = System.currentTimeMillis();
                long downloadedBytesNum = 0;
                long elapsedTime = 0;
                int itemNum = 0;
                Iterator<URL> it = urlFileMap.keySet().iterator();
                while (it.hasNext()) {
                    URL url = it.next();
                    File file = urlFileMap.get(url);
                    if (listener != null) {
                        listener.onStart(url, file);
                    }
                    long currStartTime = System.currentTimeMillis();
                    long currentDownloadedBytesNum = 0;
                    long currentElapsedTime = 0;
                    itemNum++;
                    OutputStream out = null;
                    InputStream in = null;
                    Throwable failure = null;
                    try {
                        URLConnection conn = url.openConnection(BackEnd.getProxy(Proxy.Type.HTTP));
                        conn.setConnectTimeout(timeout * 1000);
                        conn.setReadTimeout(timeout * 1000);
                        in = conn.getInputStream();
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        out = new BufferedOutputStream(new FileOutputStream(file));
                        byte[] buffer = new byte[1024];
                        int readBytesNum;
                        while (!cancel && !cancelAll && (readBytesNum = in.read(buffer)) != -1) {
                            out.write(buffer, 0, readBytesNum);
                            if (listener != null) {
                                currentDownloadedBytesNum += readBytesNum;
                                currentElapsedTime = System.currentTimeMillis() - currStartTime;
                                listener.onSingleProgress(url, file, currentDownloadedBytesNum, currentElapsedTime);
                                downloadedBytesNum += readBytesNum;
                                elapsedTime = System.currentTimeMillis() - startTime;
                                listener.onTotalProgress(itemNum, downloadedBytesNum, elapsedTime);
                            }
                        }
                    } catch (Throwable t) {
                        failure = t;
                    } finally {
                        try {
                            if (in != null) in.close();
                            if (out != null) out.close();
                        } catch (IOException ioe) {
                        }
                        if (listener != null) {
                            try {
                                if (failure != null) {
                                    listener.onFailure(url, file, failure);
                                } else if (cancel || cancelAll) {
                                    decrementTotalActiveDownloadsCount();
                                    try {
                                        listener.onCancel(url, file, downloadedBytesNum, elapsedTime);
                                    } finally {
                                        listener.onFinish(downloadedBytesNum, elapsedTime);
                                    }
                                    break;
                                } else {
                                    listener.onComplete(url, file, downloadedBytesNum, elapsedTime);
                                }
                            } finally {
                                if (!it.hasNext()) {
                                    decrementTotalActiveDownloadsCount();
                                    listener.onFinish(downloadedBytesNum, elapsedTime);
                                }
                            }
                        }
                    }
                }
            }
        });
    }
