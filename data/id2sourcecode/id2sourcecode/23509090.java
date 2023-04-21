        public void run() {
            try {
                if (release.get()) {
                    return;
                }
                Thread.sleep(timeToWait);
            } catch (InterruptedException e) {
                return;
            }
            if (ctx != null && ctx.getChannel() != null && ctx.getChannel().isConnected()) {
                ctx.setAttachment(null);
                ctx.getChannel().setReadable(true);
            }
        }
