            public void run() {
                if (listener != null) {
                    listener.onStart(url, file);
                }
                incrementTotalActiveDownloadsCount();
                long startTime = System.currentTimeMillis();
                long downloadedBytesNum = 0;
                long elapsedTime = 0;
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
                            downloadedBytesNum += readBytesNum;
                            elapsedTime = System.currentTimeMillis() - startTime;
                            listener.onSingleProgress(url, file, downloadedBytesNum, elapsedTime);
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
                    decrementTotalActiveDownloadsCount();
                    if (listener != null) {
                        try {
                            if (failure != null) {
                                listener.onFailure(url, file, failure);
                            } else if (cancel || cancelAll) {
                                listener.onCancel(url, file, downloadedBytesNum, elapsedTime);
                            } else {
                                listener.onComplete(url, file, downloadedBytesNum, elapsedTime);
                            }
                        } finally {
                            listener.onFinish(downloadedBytesNum, elapsedTime);
                        }
                    }
                }
            }
