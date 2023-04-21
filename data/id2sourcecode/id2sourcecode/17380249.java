    private void processSubscriptionRequests() {
        while (!updateSubscriptionRequests.isEmpty()) {
            SubscriptionRequest subscriptionRequest;
            synchronized (updateSubscriptionRequests) {
                subscriptionRequest = (SubscriptionRequest) updateSubscriptionRequests.remove(0);
            }
            String channelName = subscriptionRequest.getChannelName();
            DataListener listener = subscriptionRequest.getListener();
            if (subscriptionRequest.isSubscribe()) {
                subscribeSafe(channelName, listener);
            } else {
                unsubscribeSafe(channelName, listener);
            }
        }
    }
