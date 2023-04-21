    public RemoteChannel getChannel() {
        try {
            return invoker.invoke(RemoteChannel.class, "getChannel", new Class<?>[] {}, new Serializable[] {});
        } catch (IOException ex) {
            Exceptions.throwNested(RuntimeException.class, ex);
            return null;
        }
    }
