        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            LOG.warn("Client Exception Caught");
            try {
                clientHandlerContext.setClientStatusCode(ClientHandlerContext.STATUS_ERROR);
                Throwable t = e.getCause();
                clientHandlerContext.setErrorCause(t);
                String msg = "Client Error: " + t.toString();
                LOG.error(msg, t);
                ctx.getChannel().close();
            } catch (Throwable t) {
                LOG.error(t.toString(), t);
            }
            if (!exhanged.getAndSet(true)) {
                try {
                    exchanger.exchange(clientHandlerContext, sendTimeOut, TimeUnit.MILLISECONDS);
                } catch (TimeoutException te) {
                    LOG.error("The calling object did not respond");
                }
            }
        }
