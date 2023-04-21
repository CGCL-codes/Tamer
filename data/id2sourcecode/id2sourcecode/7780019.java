    public static ISOChannel getChannel(String name) throws NameRegistrar.NotFoundException {
        return (ISOChannel) NameRegistrar.get("channel." + name);
    }
