            @Override
            public void closeRequested(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
                System.out.println("Close Requested");
                e.getChannel().disconnect();
            }
