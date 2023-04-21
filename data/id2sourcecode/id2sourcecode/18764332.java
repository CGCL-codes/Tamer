    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        if (!canExecute()) {
            throw new ExecutionException("Invalid arguments in create link command");
        }
        Channel newElement = NetworkFactory.INSTANCE.createChannel();
        getContainer().getChannels().add(newElement);
        newElement.setSource(getSource());
        newElement.setTarget(getTarget());
        doConfigure(newElement, monitor, info);
        ((CreateElementRequest) getRequest()).setNewElement(newElement);
        return CommandResult.newOKCommandResult(newElement);
    }
