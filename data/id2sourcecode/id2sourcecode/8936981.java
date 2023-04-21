    public void handle(Event event) {
        try {
            event.go();
            if (isAccepted(event)) {
                new ServiceEvent(event.getChannel(), Direction.UP, this, ((SendableEvent) event).getMessage()).go();
            }
        } catch (AppiaEventException e) {
            e.printStackTrace();
        }
    }
