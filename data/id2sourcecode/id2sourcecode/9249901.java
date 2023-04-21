    @XmlElementWrapper(name = "channels")
    public List<Channel> getChannels() {
        if (channels == null) channels = new LinkedList<Channel>();
        return channels;
    }
