        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            waitingReplyChannelSet.remove(e.getChannel());
            addressTable.remove(e.getChannel());
            e.getChannel().close();
            logger.error("exceptionCaught in HttpResponseHandler", e.getCause());
        }
