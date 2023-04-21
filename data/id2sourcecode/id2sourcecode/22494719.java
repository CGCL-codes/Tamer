    @Override
    public Collection<String> getHandles() {
        Collection<String> handles = new HashSet<String>(super.getHandles());
        try {
            handles.addAll(getMainSupply().getChannelSuite().getHandles());
        } catch (NullPointerException exception) {
            System.err.println("exception getting handles from the main supply \"" + getMainSupply() + "\" for electromagnet: " + getId());
            throw exception;
        }
        return handles;
    }
