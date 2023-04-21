            public void DataArrived(ChannelHandlerContext ctx, final MessageEvent e) {
                if (server.isForwarderConnected() == true) {
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            server.outputDataArrivedToServerAndForwardedOut((BigEndianHeapChannelBuffer) e.getMessage(), e.getChannel().getId().toString());
                        }
                    });
                    server.sendDataFromForwarderOut((BigEndianHeapChannelBuffer) e.getMessage());
                } else {
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            server.outputDataArrivedToServer((BigEndianHeapChannelBuffer) e.getMessage(), e.getChannel().getId().toString());
                        }
                    });
                }
            }
