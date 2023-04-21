    @Override
    public boolean equals(Object o) {
        if (!o.getClass().equals(Subscription.class)) return false;
        Subscription s = (Subscription) o;
        return getChannel().equals(s.getChannel()) && getSubscriptionId().equals(s.getSubscriptionId());
    }
