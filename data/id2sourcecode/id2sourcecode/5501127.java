    public void addContactChannel(Address address, ContactChannel contactChannel, String userName) {
        super.save(contactChannel, userName);
        address = (Address) super.load(Address.class, address.getId());
        address.getChannels().put(contactChannel.getChannelType(), contactChannel);
    }
