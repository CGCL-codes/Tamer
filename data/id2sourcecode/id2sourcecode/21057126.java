    @Override
    public void submit(RpcController controller, ProtocolSubmitRequest request, final RpcCallback<ProtocolSubmitResponse> done) {
        WaveletName waveletName = null;
        try {
            waveletName = ModernIdSerialiser.INSTANCE.deserialiseWaveletName(request.getWaveletName());
        } catch (InvalidIdException e) {
            LOG.warning("Invalid id in submit", e);
            controller.setFailed(e.getMessage());
            return;
        }
        String channelId;
        if (request.hasChannelId()) {
            channelId = request.getChannelId();
        } else {
            channelId = null;
        }
        ParticipantId loggedInUser = asBoxController(controller).getLoggedInUser();
        frontend.submitRequest(loggedInUser, waveletName, request.getDelta(), channelId, new SubmitRequestListener() {

            @Override
            public void onFailure(String error) {
                done.run(ProtocolSubmitResponse.newBuilder().setOperationsApplied(0).setErrorMessage(error).build());
            }

            @Override
            public void onSuccess(int operationsApplied, HashedVersion hashedVersionAfterApplication, long applicationTimestamp) {
                done.run(ProtocolSubmitResponse.newBuilder().setOperationsApplied(operationsApplied).setHashedVersionAfterApplication(CoreWaveletOperationSerializer.serialize(hashedVersionAfterApplication)).build());
            }
        });
    }
