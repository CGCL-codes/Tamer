    public Address loadAddress(Long id, boolean loadCollections) {
        Address address = (Address) super.load(Address.class, id);
        if (loadCollections) {
            address.getChannels().entrySet();
        }
        return address;
    }
