    private void getChildChannels() throws RhnChannelNotFoundException, RhnConnFault {
        try {
            if (this.parent_channel_label_.isEmpty()) {
                this.child_channels_.clear();
                RhnSwChannels channels = new RhnSwChannels(this.connection_);
                for (RhnSwChannel channel : channels.getChannels()) {
                    if (channel.getParentchannel().equals(this.label_)) {
                        this.child_channels_.add(channel);
                    }
                }
            } else {
                throw new RhnChannelNotFoundException("Channel " + this.name_ + " is not a base channel");
            }
        } catch (XmlRpcException ex) {
            throw new RhnConnFault("Error connecting to spacewalk server. Problem found in connection: " + ex.getMessage());
        }
    }
