    @OneToMany(cascade = CascadeType.ALL)
    @ForeignKey(name = "Address_ContactChannel_FK")
    @JoinColumn(name = "Address_ID")
    @MapKey(name = "channelType")
    public Map<ContactChannelType, ContactChannel> getChannels() {
        return channels;
    }
