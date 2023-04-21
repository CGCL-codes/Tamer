        @Override
        public void run() {
            for (User u : UserHandler.getInstance().getAllUsers()) {
                if (u.outputBuffer.length() > 0) {
                    Comm.writeToChannel(u.getChannel(), u.outputBuffer.toString() + u.getCurrentLayer().getPrompt());
                    u.outputBuffer.delete(0, u.outputBuffer.length());
                }
            }
        }
