    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) throws Exception {
        try {
            throw e.getCause();
        } catch (final UnknownCommandException unknownCommand) {
            if (ctx.getChannel().isOpen()) {
                ctx.getChannel().write(constructHeader(MemcachedBinaryCommandDecoder.BinaryOp.Noop, null, null, null, (short) 0x0081, 0, 0));
            }
        } catch (final Throwable err) {
            logger.error("error", err);
            if (ctx.getChannel().isOpen()) {
                ctx.getChannel().close();
            }
        }
    }
