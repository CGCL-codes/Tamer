    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.warn("exceptionCaught", e.getCause());
        RpcResponse.Builder responseBuilder = RpcResponse.newBuilder();
        if (e.getCause() instanceof NoSuchServiceException) {
            responseBuilder.setErrorCode(ErrorCode.SERVICE_NOT_FOUND);
        } else if (e.getCause() instanceof NoSuchServiceMethodException) {
            responseBuilder.setErrorCode(ErrorCode.METHOD_NOT_FOUND);
        } else if (e.getCause() instanceof InvalidRpcRequestException) {
            responseBuilder.setErrorCode(ErrorCode.BAD_REQUEST_PROTO);
        } else if (e.getCause() instanceof RpcServiceException) {
            responseBuilder.setErrorCode(ErrorCode.RPC_ERROR);
        } else if (e.getCause() instanceof RpcException) {
            responseBuilder.setErrorCode(ErrorCode.RPC_FAILED);
        } else {
            logger.info("Cannot respond to handler exception", e.getCause());
            return;
        }
        RpcException ex = (RpcException) e.getCause();
        if (ex.getRpcRequest() != null && ex.getRpcRequest().hasId()) {
            responseBuilder.setId(ex.getRpcRequest().getId());
            responseBuilder.setErrorMessage(ex.getMessage());
            e.getChannel().write(responseBuilder.build());
        } else {
            logger.info("Cannot respond to handler exception", ex);
        }
    }
