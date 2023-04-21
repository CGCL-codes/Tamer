        @Override
        public void run() {
            try {
                View newView = getDispatcher().getChannel().getView();
                updateProcesses(newView);
            } catch (ChannelException e) {
                log.error(e);
                throw new RuntimeException(e);
            }
        }
