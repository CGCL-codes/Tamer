    @Override
    public Collection<String> getHandles() {
        Collection<String> handles = new HashSet<String>(super.getHandles());
        try {
            handles.addAll(getTrimSupply().getChannelSuite().getHandles());
        } catch (NullPointerException exception) {
            System.err.println("exception getting handles from the trim supply \"" + getTrimSupply() + "\" for trimmed quadrupole: " + getId());
            throw exception;
        }
        return handles;
    }
