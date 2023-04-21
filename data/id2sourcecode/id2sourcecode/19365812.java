    private Subscription getSubscription(Fiber fiber, String channel) throws MessagingException {
        Iterator<Subscription> itor = subscriptions.iterator();
        while (itor.hasNext()) {
            Subscription subscription = itor.next();
            if (subscription.getFiber() == fiber && subscription.getChannel().equals(channel)) {
                return subscription;
            }
        }
        throw new MessagingException("No subscription found");
    }
