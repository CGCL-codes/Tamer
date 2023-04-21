    public void unregister(Endpoint endpoint, Map<String, ?> properties) {
        InternalEndpoint wrapper;
        if (endpoint instanceof InternalEndpoint) {
            wrapper = (InternalEndpoint) endpoint;
            if (wrapper != null) {
                endpoint = wrappers.remove(wrapper);
                if (endpoint != null) {
                    endpoints.remove(endpoint);
                }
            }
        } else {
            wrapper = endpoints.remove(endpoint);
            if (wrapper != null) {
                wrappers.remove(wrapper);
            }
        }
        if (wrapper != null) {
            wrapper.getChannel().close();
            registry.unregister(wrapper, properties);
            for (EndpointListener listener : nmr.getListenerRegistry().getListeners(EndpointListener.class)) {
                listener.endpointUnregistered(wrapper);
            }
        }
        synchronized (this.references) {
            for (CacheableReference ref : references.keySet()) {
                ref.setDirty();
            }
        }
    }
