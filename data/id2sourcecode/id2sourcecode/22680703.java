    @Override
    public void start(final Thread synchro, final SynchronizedLoad synchronizedLoad) throws AbstractZemucanException {
        ExecutionState execState = ExecutionState.INITIATED;
        if (Boolean.parseBoolean(Configurator.getInstance().getProperty(AbstractInterfaceController.SHOW_LICENSE))) {
            this.output.writeLine(AboutNode.readAboutInformation());
        }
        if (Boolean.parseBoolean(Configurator.getInstance().getProperty(AbstractInterfaceController.SHOW_INTRO_PROPERTY))) {
            this.output.writeLine(Messages.getString("ImplementationSystemInterfaceController.Instructions"));
        }
        if (synchro != null) {
            synchronized (synchro) {
                synchronizedLoad.alreadyLoaded();
                synchro.notifyAll();
            }
        }
        while (execState != ExecutionState.APPLICATION_EXIT) {
            final String phrase = this.input.readString();
            final int index = phrase.indexOf('?');
            if (index < 0) {
                execState = ImplementationExecuter.getInstance().execute(phrase, this.output);
            } else {
                final String newPhrase = phrase.substring(0, index);
                final ReturnOptions actual = InterfaceCore.analyzePhrase(newPhrase);
                this.output.writeLine(actual.toString());
            }
        }
    }
