        public ProxyServiceCall(ServiceCall serviceCall, ServiceResponse serviceResponse, UOSMessageContext messageContextBefore) {
            this.serviceCall = serviceCall;
            this.serviceResponse = serviceResponse;
            this.messageContextBefore = messageContextBefore;
            this.numberChannels = serviceCall.getChannels();
        }
