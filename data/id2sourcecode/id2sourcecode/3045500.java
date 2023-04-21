    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        try {
            throw e.getCause();
        } catch (UnknownCommandException unknownCommand) {
            if (ctx.getChannel().isOpen()) ctx.getChannel().write(constructHeader(MemcachedBinaryCommandDecoder.BinaryCommand.Noop, null, null, null, (short) 0x0081, 0, 0));
        } catch (Throwable err) {
            logger.error("error", err);
            if (ctx.getChannel().isOpen()) ctx.getChannel().close();
        }
    }
