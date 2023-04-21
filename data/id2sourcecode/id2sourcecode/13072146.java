    public static void action(MessageContext context, String message) {
        Session session = context.getSession();
        if (context.isPrivateMessage()) {
            session.sayPrivate(context.getSender(), " " + message);
        } else {
            session.getChannel(context.getChannel()).action(message);
        }
    }
