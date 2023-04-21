    public IResponse dispatch(IRequest request) throws WebApiException {
        if (request == null) {
            throw new IllegalArgumentException("Error request == null");
        }
        IResponse response = null;
        try {
            ClientResource resource = getResource(request);
            final Representation handle = resource.handle();
            if (handle == null) {
                throw new WebApiCommunicationError();
            }
            final DataDigster dataDigster = new DataDigster(request, handle);
            final Status status = resource.getStatus();
            int responseCode = status.getCode();
            if (!dataDigster.validateResponse()) {
                throw new InvalidResponseException(new Exception("Invalid response media type for XML response"));
            }
            if (!request.isExpectedResponseCode(responseCode)) {
                throw new UnexpectedResponseCode(responseCode, handle);
            }
            dataDigster.digest();
            response = ResponseFactory.createResponse(request, responseCode, dataDigster.getResponseData());
        } catch (ResourceException e) {
            throw new InternalWebApiException(e);
        }
        return response;
    }
