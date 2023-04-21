    protected void execute() {
        ISqlReadCursor<Data> cursor = null;
        try {
            int dataCount = 0;
            long maxDataToRoute = context.getChannel().getMaxDataToRoute();
            int peekAheadCount = symmetricDialect.getRouterDataPeekAheadCount();
            String lastTransactionId = null;
            List<Data> peekAheadQueue = new ArrayList<Data>(peekAheadCount);
            boolean nontransactional = context.getChannel().getBatchAlgorithm().equals("nontransactional") || !symmetricDialect.supportsTransactionId();
            cursor = prepareCursor();
            boolean moreData = true;
            while (dataCount <= maxDataToRoute || lastTransactionId != null) {
                if (moreData) {
                    moreData = fillPeekAheadQueue(peekAheadQueue, peekAheadCount, cursor);
                }
                if ((lastTransactionId == null || nontransactional) && peekAheadQueue.size() > 0) {
                    Data data = peekAheadQueue.remove(0);
                    copyToQueue(data);
                    dataCount++;
                    lastTransactionId = data.getTransactionId();
                } else if (lastTransactionId != null && peekAheadQueue.size() > 0) {
                    Iterator<Data> datas = peekAheadQueue.iterator();
                    int dataWithSameTransactionIdCount = 0;
                    while (datas.hasNext()) {
                        Data data = datas.next();
                        if (lastTransactionId.equals(data.getTransactionId())) {
                            dataWithSameTransactionIdCount++;
                            datas.remove();
                            copyToQueue(data);
                            dataCount++;
                        }
                    }
                    if (dataWithSameTransactionIdCount == 0) {
                        lastTransactionId = null;
                    }
                } else if (peekAheadQueue.size() == 0) {
                    break;
                }
            }
        } catch (Throwable ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            copyToQueue(new EOD());
            reading = false;
        }
    }
