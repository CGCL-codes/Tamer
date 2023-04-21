    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.error("Unexpected exception from downstream while get information: " + firstMessage, e.getCause());
        if (firstMessage) {
            firstMessage = false;
            result.set(LocalExecDefaultResult.BadTransmition);
            result.exception = (Exception) e.getCause();
            back = new StringBuilder("Error in LocalExec: ");
            back.append(result.exception.getMessage());
            back.append('\n');
        } else {
            back.append("\nERROR while receiving answer: ");
            result.exception = (Exception) e.getCause();
            back.append(result.exception.getMessage());
            back.append('\n');
        }
        e.getChannel().close();
    }
