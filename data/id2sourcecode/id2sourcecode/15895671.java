    protected void getJarInfo(File dir) throws Exception {
        setState(STATE_CHECKING_CACHE);
        filesLastModified = new HashMap<String, Long>();
        fileSizes = new int[urlList.length];
        File timestampsFile = new File(dir, "timestamps");
        if (timestampsFile.exists()) {
            setState(STATE_CHECKING_FOR_UPDATES);
            filesLastModified = readHashMapFile(timestampsFile);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(concurrentLookupThreads);
        Queue<Future> requests = new LinkedList<Future>();
        final Object sync = new Integer(1);
        for (int j = 0; j < urlList.length; j++) {
            final int i = j;
            Future request = executorService.submit(new Runnable() {

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
            });
            requests.add(request);
        }
        while (!requests.isEmpty()) {
            Iterator<Future> iterator = requests.iterator();
            while (iterator.hasNext()) {
                Future request = iterator.next();
                if (request.isDone()) {
                    request.get();
                    iterator.remove();
                    percentage = 5 + (int) (10 * (urlList.length - requests.size()) / (float) urlList.length);
                }
            }
            Thread.sleep(10);
        }
        executorService.shutdown();
    }
