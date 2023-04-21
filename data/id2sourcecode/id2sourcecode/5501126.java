    public void addContactChannel(Long addressID, ContactChannel contactChannel, String userName) {
        super.save(contactChannel, userName);
        Address address = (Address) super.load(Address.class, addressID);
        address.getChannels().put(contactChannel.getChannelType(), contactChannel);
    }
