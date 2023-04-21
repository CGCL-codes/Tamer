    @Override
    protected final void respond(final AjaxRequestTarget target) {
        final Map<String, String[]> map = ((WebRequestCycle) RequestCycle.get()).getRequest().getParameterMap();
        final Map<String, String> eventAttribute = new HashMap<String, String>();
        for (final Map.Entry<String, String[]> entry : map.entrySet()) {
            eventAttribute.put(entry.getKey(), entry.getValue()[0]);
        }
        final CometdTarget cTarget = new CometdTarget(target);
        listener.onEvent(getChannelId(), eventAttribute, cTarget);
    }
