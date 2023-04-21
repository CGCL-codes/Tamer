    @SuppressWarnings("unchecked")
    public static ChannelService getChannelService(String className) {
        try {
            Class<? extends ChannelService> clazz = (Class<? extends ChannelService>) Class.forName(className);
            return impl = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
