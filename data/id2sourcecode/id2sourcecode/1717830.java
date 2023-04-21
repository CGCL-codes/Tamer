    private void sendEmptyResponse(DPWSContextImpl context) throws DPWSException {
        context.getExchange().getInMessage().getChannel().sendEmptyResponse(context);
    }
