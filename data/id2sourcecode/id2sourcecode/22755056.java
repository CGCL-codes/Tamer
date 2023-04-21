    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WebservicesChannelImpl)) {
            return false;
        }
        WebservicesChannelImpl webservicesChannel = (WebservicesChannelImpl) obj;
        return new EqualsBuilder().append(this.userId, webservicesChannel.getUserId()).append(this.channelId, webservicesChannel.getChannelId()).isEquals();
    }
