    @Override
    protected void validate() {
        if (isValid()) return;
        super.validate();
        if (defaultChannels != null) {
            for (Iterator<String> iter = defaultChannels.iterator(); iter.hasNext(); ) {
                String id = iter.next();
                if (!getMessageBroker().getChannelIds().contains(id)) {
                    iter.remove();
                    if (Log.isWarn()) {
                        Log.getLogger(getLogCategory()).warn("Removing the Channel " + id + " from Destination " + getId() + "as MessageBroker does not know the channel");
                    }
                }
            }
        } else {
            defaultChannels = getMessageBroker().getDefaultChannels();
        }
    }
