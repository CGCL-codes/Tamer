    protected void fireEvent(ChanneledEvent event) throws EventVetoException {
        Method method = getChannelMap().get(event.getEventType());
        try {
            List<EventListener> listeners = getListeners();
            for (EventListener listener : listeners) {
                method.invoke(listener, new Object[] { event });
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to access " + method, e);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof EventVetoException) {
                throw (EventVetoException) e.getTargetException();
            }
            throw new EventBusException("unable to deliver event on channel " + event.getEventType(), e);
        }
    }
