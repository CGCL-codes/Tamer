    public RpcDispatcher getDispatcher() throws ChannelException {
        if (dispatcher == null) {
            dispatcher = new RpcDispatcher(getConnectedChannel(), null, null, new ClientHandler());
            setView(dispatcher.getChannel().getView());
            if (isRunLocalExec()) {
                Executive exec = new Executive();
                exec.setClusterName(getClusterName());
                exec.setMaxGroupSize(0);
                setLocalExec(exec);
                exec.start();
            }
            timer = new Timer(true);
            timer.schedule(new ChannelTimer(), 0, 5000);
        }
        return dispatcher;
    }
