    @Override
    public void run() {
        try {
            log.debug("Running process for:" + getProcessId());
            View view = getExecutive().getRpcDispatcher().getChannel().getView();
            Set<Address> group = new HashSet<Address>();
            group.addAll(view.getMembers());
            group.remove(getExecutive().getRpcDispatcher().getChannel().getLocalAddress());
            group.remove(getProcessId().getSource());
            if (getExecutive().getMaxGroupSize() < 0 || group.size() <= getExecutive().getMaxGroupSize()) {
                JatherHandlerStub stub = new JatherHandlerStub(getExecutive().getRpcDispatcher(), getProcessId().getSource());
                ProcessContext result = stub.processRequest(new RequestContext(getProcessId(), getExecutive().getChannel().getLocalAddress()));
                ChannelClassLoader cl = new ChannelClassLoader(getExecutive().getRpcDispatcher(), getProcessId().getSource());
                Callable<?> callable = result.getCallable(cl);
                if (callable != null) {
                    log.debug("Executing: " + callable);
                    try {
                        result.setResult(callable.call());
                    } catch (Exception e) {
                        result.setCallableException(e);
                    }
                    stub.processResult(result);
                }
            }
            getExecutive().getProcessMap().remove(getProcessId());
        } catch (Throwable e) {
            log.error(getProcessDefinition(), e);
            setInnerThrowable(e);
        }
    }
