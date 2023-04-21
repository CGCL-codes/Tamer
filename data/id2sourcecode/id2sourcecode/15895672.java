                public void run() {
                    try {
                        URLConnection urlconnection = urlList[i].openConnection();
                        urlconnection.setDefaultUseCaches(false);
                        if (urlconnection instanceof HttpURLConnection) {
                            ((HttpURLConnection) urlconnection).setRequestMethod("HEAD");
                        }
                        fileSizes[i] = urlconnection.getContentLength();
                        long lastModified = urlconnection.getLastModified();
                        String fileName = getFileName(urlList[i]);
                        if (cacheEnabled && lastModified != 0 && filesLastModified.containsKey(fileName)) {
                            long savedLastModified = filesLastModified.get(fileName);
                            if (savedLastModified == lastModified) {
                                fileSizes[i] = -2;
                            }
                        }
                        if (fileSizes[i] >= 0) {
                            synchronized (sync) {
                                totalSizeDownload += fileSizes[i];
                            }
                        }
                        filesLastModified.put(fileName, lastModified);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to fetch information for " + urlList[i], e);
                    }
                }
