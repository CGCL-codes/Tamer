    @Override
    public void start(final Thread synchro, final SynchronizedLoad synchronizedLoad) throws AbstractZemucanException {
        String phrase = null;
        if (Boolean.parseBoolean(Configurator.getInstance().getProperty(AbstractInterfaceController.SHOW_LICENSE))) {
            this.output.writeLine(AboutNode.readAboutInformation());
        }
        if (Boolean.parseBoolean(Configurator.getInstance().getProperty(AbstractInterfaceController.SHOW_INTRO_PROPERTY))) {
            this.output.writeLine(Messages.getString("ImplementationJlineInterfaceController" + ".Instructions"));
        }
        ExecutionState execState = ExecutionState.INITIATED;
        ImplementationJlineInterfaceController.LOGGER.debug("User interaction start");
        if (synchro != null) {
            synchronized (synchro) {
                synchronizedLoad.alreadyLoaded();
                synchro.notifyAll();
            }
        }
        while (execState != ExecutionState.APPLICATION_EXIT) {
            phrase = this.input.readString();
            try {
                execState = ImplementationExecuter.getInstance().execute(phrase, this.output);
            } catch (final ExecutingCommandException e) {
                ImplementationJlineInterfaceController.LOGGER.error(e.getMessage());
                ImplementationJlineInterfaceController.LOGGER.error(e.getCause().getMessage());
            }
        }
    }
