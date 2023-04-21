        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            e.getChannel().close();
            logger.error("exceptionCaught in HttpResponseHandler", e.getCause());
            updateSSLProxyConnectionStatus(DISCONNECTED);
            waitingResponse.set(false);
            close();
            clearBuffer();
        }
