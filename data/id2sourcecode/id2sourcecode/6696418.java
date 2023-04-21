    public void run() {
        try {
            long lTime = 0L;
            long lAvg = 0L;
            for (int j = 0; bContinue && (iHits == -1 || j < iHits); j++) {
                lAvg = 0L;
                for (int i = 0; i < urls.length; i++) {
                    Date reqStart = new Date();
                    urlConn = (new URL(urls[i])).openConnection();
                    urlConn.setUseCaches(false);
                    urlConn.setAllowUserInteraction(true);
                    urlConn.connect();
                    is = urlConn.getInputStream();
                    lRead += drainStream(is);
                    Date reqStop = new Date();
                    lTime = reqStop.getTime() - reqStart.getTime();
                    lAvg += lTime;
                    lMaxRequest = Math.max(lMaxRequest, lTime);
                    lMinRequest = Math.min(lMinRequest, lTime);
                    lRequests++;
                    Thread.currentThread().sleep(iPause);
                }
                if (iHits > 0) {
                    alResponseTimes[j] = lAvg / urls.length;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < iHits; i++) {
            lAverage += alResponseTimes[i];
        }
        lAverage /= iHits;
        synchronized (objClassLock) {
            lReadTotal += lRead;
            lTotalRequests += lRequests;
            lMaxRequestTime = Math.max(lMaxRequestTime, lMaxRequest);
            lMinRequestTime = Math.min(lMinRequestTime, lMinRequest);
        }
        lReadTotal += lRead;
    }
