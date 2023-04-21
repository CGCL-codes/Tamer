    private Long getChannelId() {
        if (reusableChannels.size() <= 0) {
            return new Long(nextChannelId++);
        } else {
            return (Long) reusableChannels.iterator().next();
        }
    }
