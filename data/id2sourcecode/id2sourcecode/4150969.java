    protected Channel getChannel(String name) {
        try {
            return (Channel) dataService.getBinding(name);
        } catch (ObjectNotFoundException e) {
            return null;
        }
    }
