    public void singleLineReportTo(PrintWriter writer) {
        Iterator it = sortedHostQueues.iterator();
        int total = 0;
        int ready = 0;
        int snoozed = 0;
        int empty = 0;
        int busy = 0;
        while (it.hasNext()) {
            AdaptiveRevisitHostQueueWrapper wrapper = (AdaptiveRevisitHostQueueWrapper) it.next();
            total++;
            switch(wrapper.hq.getState()) {
                case AdaptiveRevisitHostQueue.HQSTATE_BUSY:
                    busy++;
                    break;
                case AdaptiveRevisitHostQueue.HQSTATE_EMPTY:
                    empty++;
                    break;
                case AdaptiveRevisitHostQueue.HQSTATE_READY:
                    ready++;
                    break;
                case AdaptiveRevisitHostQueue.HQSTATE_SNOOZED:
                    snoozed++;
                    break;
            }
        }
        writer.print(total + " queues: " + ready + " ready, " + snoozed + " snoozed, " + busy + " busy, and " + empty + " empty");
    }
