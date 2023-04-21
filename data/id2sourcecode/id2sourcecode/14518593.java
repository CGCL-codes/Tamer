    public void run() {
        try {
            logger.info("Retrieved Wiki Names =>: " + topicNames.size());
            pLogger = new BufferedWriter(new FileWriter(this.parserLogger));
            for (String topicName : topicNames) {
                long startTime = System.currentTimeMillis();
                if (this.fetchMode.equalsIgnoreCase("web")) {
                    String requestUrl = String.format("%s/%s/%s", appUrl, virtualWiki, Utilities.encodeAndEscapeTopicName(topicName));
                    logger.info(String.format("Thread[%d] Requesting Web Page: %s", threadId, requestUrl));
                    fetchFromWeb(requestUrl);
                    long endTime = System.currentTimeMillis();
                    long fetchTime = endTime - startTime;
                    logger.info(String.format("Thread[%d] Retrieved Web Page: %s in: %s ms", threadId, requestUrl, fetchTime));
                    System.out.println(String.format("Thread[%d] Retrieved Web Page: %s in: %s ms", threadId, requestUrl, fetchTime));
                } else if (this.fetchMode.equalsIgnoreCase("db")) {
                    logger.info(String.format("Thread[%d] Requesting Topic: %s", threadId, topicName));
                    boolean rv = fetchFromDb(virtualWiki, topicName);
                    long endTime = System.currentTimeMillis();
                    long fetchTime = endTime - startTime;
                    if (!rv) {
                        pLogger.write("OK|" + threadId + "|" + topicName + "|" + fetchTime + "\n");
                    } else {
                        pLogger.write("ERROR|" + threadId + "|" + topicName + "|" + fetchTime + "\n");
                    }
                    pLogger.flush();
                    logger.info(String.format("Thread[%d] Retrieved Topic: %s in: %s ms", threadId, topicName, fetchTime));
                    System.out.println(String.format("Thread[%d] Retrieved Topic: %s in: %s ms", threadId, topicName, fetchTime));
                } else {
                    logger.error("Unknow fetch mode!");
                    return;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                pLogger.flush();
                pLogger.close();
            } catch (Exception ex) {
            }
        }
    }
