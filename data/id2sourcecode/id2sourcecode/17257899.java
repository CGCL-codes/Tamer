    protected CamelMediationBinding getCamelMediationBinding(String b) {
        CamelMediationBinding binding = bindingRegistry.get(b);
        if (binding == null) {
            MediationBindingFactory factory = channel.getIdentityMediator().getBindingFactory();
            if (factory == null) throw new IllegalArgumentException("No configured Mediation Binding Factory in mediator");
            if (logger.isTraceEnabled()) logger.trace("Attempting to create binding for " + b + " with factory " + factory);
            binding = (CamelMediationBinding) factory.createBinding(b, getChannel());
            if (logger.isTraceEnabled()) logger.trace("Created binding " + binding + " for " + b + " with factory " + factory);
            if (binding != null) {
                bindingRegistry.put(b, binding);
            } else throw new IllegalArgumentException("Factory " + factory + " does not support binding " + b);
        }
        return binding;
    }
