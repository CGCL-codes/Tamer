    protected boolean doExecute() throws java.io.IOException {
        if (!getInputs().isEmpty()) {
            Port.RbnbPort rbnbPort = (Port.RbnbPort) getInputs().get(0);
            addArgument("-a");
            addArgument(rbnbPort.getPort());
            addArgument("-i");
            addArgument(rbnbPort.getChannel());
        }
        if (!getOutputs().isEmpty()) {
            Port.RbnbPort rbnbPort = (Port.RbnbPort) getOutputs().get(0);
            addArguments("-A", rbnbPort.getPort());
            if (rbnbPort.getName() != null) addArguments("-o", rbnbPort.getName());
            if (rbnbPort.getCacheFrames() > 0) addArguments("-c", String.valueOf(rbnbPort.getCacheFrames()));
            if (rbnbPort.getArchiveFrames() > 0) {
                if ("create".equals(rbnbPort.getArchiveMode())) addArguments("-K", String.valueOf(rbnbPort.getArchiveFrames())); else addArguments("-k", String.valueOf(rbnbPort.getArchiveFrames()));
            }
        }
        return super.doExecute();
    }
