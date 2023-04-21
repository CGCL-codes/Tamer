        public HttpResponse execute(HttpRequest request) throws GadgetException {
            lastHttpRequest = request;
            if (request.getGadget() == null) {
                throw new GadgetException(GadgetException.Code.FAILED_TO_RETRIEVE_CONTENT, "No gadget associated with rendering request.");
            }
            if (request.getContainer() == null) {
                throw new GadgetException(GadgetException.Code.FAILED_TO_RETRIEVE_CONTENT, "No container associated with rendering request.");
            }
            if (request.getSecurityToken() == null) {
                throw new GadgetException(GadgetException.Code.FAILED_TO_RETRIEVE_CONTENT, "No security token associated with rendering request.");
            }
            if (request.getOAuthArguments() == null) {
                throw new GadgetException(GadgetException.Code.FAILED_TO_RETRIEVE_CONTENT, "No oauth arguments associated with rendering request.");
            }
            assertTrue(request.getOAuthArguments().isProxiedContentRequest());
            HttpResponse response;
            switch(request.getAuthType()) {
                case NONE:
                    response = plainResponses.get(request.getUri());
                    break;
                case SIGNED:
                    response = signedResponses.get(request.getUri());
                    break;
                case OAUTH:
                    response = oauthResponses.get(request.getUri());
                    break;
                default:
                    response = null;
                    break;
            }
            if (response == null) {
                throw new GadgetException(GadgetException.Code.FAILED_TO_RETRIEVE_CONTENT, "Unknown file: " + request.getUri());
            }
            return response;
        }
