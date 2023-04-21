    @SuppressWarnings("unchecked")
    public void run() {
        boolean trace = log.isTraceEnabled();
        List<LuceneWork> filteredQueue = new ArrayList<LuceneWork>(queue);
        if (trace) {
            log.trace("Preparing {} Lucene works to be sent to master node.", filteredQueue.size());
        }
        for (LuceneWork work : queue) {
            if (work instanceof OptimizeLuceneWork) {
                filteredQueue.remove(work);
            }
        }
        if (trace) {
            log.trace("Filtering: optimized Lucene works are not going to be sent to master node. There is {} Lucene works after filtering.", filteredQueue.size());
        }
        if (filteredQueue.isEmpty()) {
            if (trace) {
                log.trace("Nothing to send. Propagating works to a cluster has been skipped.");
            }
            return;
        }
        try {
            Message message = new Message(null, factory.getAddress(), (Serializable) filteredQueue);
            factory.getChannel().send(message);
            if (trace) {
                log.trace("Lucene works have been sent from slave {} to master node.", factory.getAddress());
            }
        } catch (ChannelNotConnectedException e) {
            throw new SearchException("Unable to send Lucene work. Channel is not connected to: " + factory.getClusterName());
        } catch (ChannelClosedException e) {
            throw new SearchException("Unable to send Lucene work. Attempt to send message on closed JGroups channel");
        }
    }
