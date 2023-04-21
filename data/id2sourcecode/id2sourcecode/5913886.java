    @SuppressWarnings("unchecked")
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent me) {
        Object obj = me.getMessage();
        if (!(obj instanceof Operation<?>)) {
            me.getChannel().close();
        }
        Channel c = me.getChannel();
        Operation<Object> op = (Operation<Object>) obj;
        try {
            Object result = op.perform();
            if (result == null) {
                c.write(Null.NULL);
            } else {
                c.write(result);
            }
        } catch (Exception e) {
            OperationExecutionException oee = new OperationExecutionException(e);
            c.write(oee);
        }
    }
