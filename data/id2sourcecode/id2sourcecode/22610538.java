    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        try {
            throw e.getCause();
        } catch (ClientException ce) {
            if (ctx.getChannel().isOpen()) ctx.getChannel().write(ChannelBuffers.wrappedBuffer(CLIENT_ERROR.array(), ce.getMessage().getBytes(), CRLF.array()));
        } catch (ClosedChannelException e2) {
            logger.info("ClosedChannelException" + e.getChannel().getRemoteAddress());
            if (ctx.getChannel().isOpen()) {
                ctx.getChannel().write(ERROR);
            }
        } catch (IOException e2) {
            StackTraceElement[] stackTraceElements = e2.getStackTrace();
            for (int i = 0; i < stackTraceElements.length; i++) {
                if (stackTraceElements[i].getClassName().equals("sun.nio.ch.SocketDispatcher")) {
                    logger.info("IOException:" + e.getChannel().getRemoteAddress());
                    if (ctx.getChannel().isOpen()) {
                        ctx.getChannel().write(ERROR);
                    }
                    return;
                }
            }
            logger.error("error", e2);
        } catch (Throwable tr) {
            logger.error("error", tr);
            if (ctx.getChannel().isOpen()) {
                ctx.getChannel().write(ERROR);
            }
        }
    }
