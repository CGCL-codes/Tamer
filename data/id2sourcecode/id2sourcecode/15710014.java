    AsteriskChannelImpl getChannelImplByName(String name) {
        Date dateOfCreation = null;
        AsteriskChannelImpl channel = null;
        if (name == null) {
            return null;
        }
        synchronized (channels) {
            for (AsteriskChannelImpl tmp : channels) {
                if (tmp.getName() != null && tmp.getName().equals(name)) {
                    if (dateOfCreation == null || tmp.getDateOfCreation().after(dateOfCreation) || (tmp.getDateOfCreation().equals(dateOfCreation) && tmp.getState() != ChannelState.HUNGUP)) {
                        channel = tmp;
                        dateOfCreation = channel.getDateOfCreation();
                    }
                }
            }
        }
        return channel;
    }
