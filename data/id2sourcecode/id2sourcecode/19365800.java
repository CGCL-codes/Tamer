    @Override
    public List<org.slasoi.common.messaging.pubsub.Subscription> getSubscriptions() throws MessagingException {
        List<org.slasoi.common.messaging.pubsub.Subscription> subs = new ArrayList<org.slasoi.common.messaging.pubsub.Subscription>();
        Iterator<Subscription> itor = getSubscriptions(fiber).iterator();
        while (itor.hasNext()) {
            Subscription subscription = itor.next();
            subs.add(new org.slasoi.common.messaging.pubsub.Subscription(null, subscription.getChannel()));
        }
        return subs;
    }
