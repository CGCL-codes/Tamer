        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                future.getChannel().write("Welcome to " + InetAddress.getLocalHost().getHostName() + " secure chat service!\n");
                future.getChannel().write("Your session is protected by " + sslHandler.getEngine().getSession().getCipherSuite() + " cipher suite.\n");
                channels.add(future.getChannel());
            } else {
                future.getChannel().close();
            }
        }
