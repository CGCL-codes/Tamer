    @ManyToOne(cascade = CascadeType.PERSIST)
    @ForeignKey(name = "ContactCh_ContactChType_FK")
    @JoinColumn(name = "ContactChannelType")
    public ContactChannelType getChannelType() {
        return channelType;
    }
