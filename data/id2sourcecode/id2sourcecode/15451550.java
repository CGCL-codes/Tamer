        @Override
        public void internalFrameClosing(InternalFrameEvent event) {
            if (!frame.getChannelName().equals(ChatCommands.GLOBAL_CHANNEL_NAME)) {
                frame.myChatClient.leaveChannel(frame.getChannelName());
            } else {
                frame.myChatClient.showMessage("No puede abandonar el canal Global. Debe cerrar el chat principal");
            }
        }
