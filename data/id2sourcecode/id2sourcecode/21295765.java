    public static <T extends IMessage> BlockingHook createAwaitMessageHook(final SelectableChannel channel, final Class<T> msgClazz) {
        IPredicate<IEnvelope> pred = new IPredicate<IEnvelope>() {

            public boolean appliesTo(IEnvelope obj) {
                return obj.getChannel().equals(channel) && obj.getMessage().getClass().equals(msgClazz);
            }
        };
        return new BlockingHook(pred);
    }
