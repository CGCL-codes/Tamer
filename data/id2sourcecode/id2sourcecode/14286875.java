        public void channelConnected(Channel channel) {
            String gc = channel.getChannelName();
            switch(stat) {
                case SPEER:
                    if (gc.equals("MANET")) {
                        sPeerCLocalLoop();
                    } else if (gc.equals("SPEER")) {
                        sPeerCSPeerLoop();
                    }
                    break;
                case PEER:
                    if (gc.equals("MANET")) {
                        peerLoop();
                    }
                    break;
            }
        }
