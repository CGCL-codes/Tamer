        protected void handleInput(IRCWindow window, String[] tokens) {
            Channel selectedChannel = window.getChannel();
            selectedChannel.part("[SandIRC Client Leaving...] Go and get it, you know you want it...");
        }
