    public Channel getChannel(String contactPublicSSKKey) {
        Iterator iter = channels.iterator();
        while (iter.hasNext()) {
            Channel channel = (Channel) iter.next();
            if (channel.getContactPublicSSKKey().equals(contactPublicSSKKey)) {
                return channel;
            }
        }
        return null;
    }
