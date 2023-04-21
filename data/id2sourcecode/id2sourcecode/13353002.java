    private void init(String mulegroupBinding, String name) {
        DataManager dman = AppContext.getDataManager();
        dman.markForUpdate(this);
        binding = mulegroupBinding;
        dman.setBinding(binding, this);
        channel = AppContext.getChannelManager().createChannel(name, this, Delivery.RELIABLE);
    }
