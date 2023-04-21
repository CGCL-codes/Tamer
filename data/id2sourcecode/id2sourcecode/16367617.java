    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        final RpcRequest request = (RpcRequest) e.getMessage();
        String serviceName = request.getServiceName();
        String methodName = request.getMethodName();
        logger.info("Received request for serviceName: " + serviceName + ", method: " + methodName);
        if (request.getIsBlockingService()) {
            BlockingService blockingService = blockingServiceMap.get(serviceName);
            if (blockingService == null) {
                throw new NoSuchServiceException(request, serviceName);
            } else if (blockingService.getDescriptorForType().findMethodByName(methodName) == null) {
                throw new NoSuchServiceMethodException(request, methodName);
            } else if (!request.hasId()) {
                throw new NoRequestIdException();
            } else {
                MethodDescriptor methodDescriptor = blockingService.getDescriptorForType().findMethodByName(methodName);
                Message methodRequest = null;
                try {
                    methodRequest = buildMessageFromPrototype(blockingService.getRequestPrototype(methodDescriptor), request.getRequestMessage());
                } catch (InvalidProtocolBufferException ex) {
                    throw new InvalidRpcRequestException(ex, request, "Could not build method request message");
                }
                RpcController controller = new NettyRpcController();
                Message methodResponse = null;
                try {
                    methodResponse = blockingService.callBlockingMethod(methodDescriptor, controller, methodRequest);
                } catch (ServiceException ex) {
                    throw new RpcServiceException(ex, request, "BlockingService RPC call threw ServiceException");
                } catch (Exception ex) {
                    throw new RpcException(ex, request, "BlockingService threw unexpected exception");
                }
                if (controller.failed()) {
                    throw new RpcException(request, "BlockingService RPC failed: " + controller.errorText());
                } else if (methodResponse == null) {
                    throw new RpcException(request, "BlockingService RPC returned null response");
                }
                RpcResponse response = NettyRpcProto.RpcResponse.newBuilder().setId(request.getId()).setResponseMessage(methodResponse.toByteString()).build();
                e.getChannel().write(response);
            }
        } else {
            Service service = serviceMap.get(serviceName);
            if (service == null) {
                throw new NoSuchServiceException(request, serviceName);
            } else if (service.getDescriptorForType().findMethodByName(methodName) == null) {
                throw new NoSuchServiceMethodException(request, methodName);
            } else {
                MethodDescriptor methodDescriptor = service.getDescriptorForType().findMethodByName(methodName);
                Message methodRequest = null;
                try {
                    methodRequest = buildMessageFromPrototype(service.getRequestPrototype(methodDescriptor), request.getRequestMessage());
                } catch (InvalidProtocolBufferException ex) {
                    throw new InvalidRpcRequestException(ex, request, "Could not build method request message");
                }
                final Channel channel = e.getChannel();
                final RpcController controller = new NettyRpcController();
                RpcCallback<Message> callback = !request.hasId() ? null : new RpcCallback<Message>() {

                    public void run(Message methodResponse) {
                        if (methodResponse != null) {
                            channel.write(RpcResponse.newBuilder().setId(request.getId()).setResponseMessage(methodResponse.toByteString()).build());
                        } else {
                            logger.info("service callback returned null message");
                            RpcResponse.Builder builder = RpcResponse.newBuilder().setId(request.getId()).setErrorCode(ErrorCode.RPC_ERROR);
                            if (controller.errorText() != null) {
                                builder.setErrorMessage(controller.errorText());
                            }
                            channel.write(builder.build());
                        }
                    }
                };
                try {
                    service.callMethod(methodDescriptor, controller, methodRequest, callback);
                } catch (Exception ex) {
                    throw new RpcException(ex, request, "Service threw unexpected exception");
                }
            }
        }
    }
