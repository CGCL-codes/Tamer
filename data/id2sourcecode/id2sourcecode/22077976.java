    public static void invoke(Object... args) {
        String eventName = (String) args[0];
        int len = args.length - 1;
        Object[] eventArgs = new Object[len];
        for (int i = 0; i < len; i++) {
            eventArgs[i] = args[i + 1];
        }
        List<Event> events = EventFactory.getInstance().getEvents(eventName);
        if (events != null) {
            for (Event event : events) {
                try {
                    event.execute(eventArgs);
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }
    }
