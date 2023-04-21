        @Override
        public void stop(Gamio gamio) {
            log.info("Stopping Gamio...");
            Context context = Context.getInstance();
            context.getTimer().cancel();
            context.getTimer().purge();
            MessageQueue msgQueue = context.getMessageQueue();
            if (msgQueue != null) msgQueue.stop();
            ServerManager serverManager = context.getServerManager();
            if (serverManager != null) serverManager.stop();
            ClientManager clientManager = context.getClientManager();
            if (clientManager != null) clientManager.stopAllClients();
            Workshop workshop = context.getWorkshop();
            if (workshop != null) workshop.stop();
            ChannelManager channelManager = context.getChannelManager();
            if (channelManager != null) channelManager.stop();
            ProcessorManager processorManager = context.getProcessorManager();
            if (processorManager != null) processorManager.stopAllProcessors();
            InstProps serviceProps = context.getInstProps();
            context.clear();
            gamio.changeState(GamioStopped.getInstance());
            log.info("Gamio[name<", serviceProps.getName(), ">, id<", serviceProps.getId(), ">] stopped");
        }
