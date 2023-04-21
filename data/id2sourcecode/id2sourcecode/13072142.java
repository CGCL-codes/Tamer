    public static void say(MessageContext context, String message) {
        if (context.isPrivateMessage()) {
            context.getSession().sayPrivate(context.getSender(), message);
        } else {
            context.getSession().getChannel(context.getChannel()).say(context.getUserTarget() + ": " + message);
        }
    }
