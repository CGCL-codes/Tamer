    public void processMessage(MessageEvent messageEvent) {
        String payload = null;
        PubSubRequest request = null;
        try {
            PubSubMessage message = messageEvent.getMessage();
            payload = message.getPayload();
            log.trace(String.format("New message arrived on the channel %s. Message payload: %s", message.getChannelName(), Utils.shortenString(payload, 1000)));
            JSONObject o;
            try {
                request = PubSubRequest.fromJson(payload);
            } catch (Exception e) {
                throw new Exception(String.format("Invalid pubsub message received: %s", e.getMessage()));
            }
            log.trace("Request type: " + request.getRequestType());
            PubSubResponse response;
            if (request.getRequestType().equals("SysInfoRequest")) {
                SysInfoRequest sysInfoRequest = SysInfoRequest.fromJson(payload);
                response = MonitoringDataRequestHandler.processRequest(sysInfoRequest);
            } else if (request.getRequestType().equals("InfrastructureInfoRequest")) {
                InfrastructureInfoRequest infrastructureInfoRequest = InfrastructureInfoRequest.fromJson(payload);
                response = MonitoringDataRequestHandler.processRequest(infrastructureInfoRequest);
            } else if (request.getRequestType().equals("GetServicesInfoRequest")) {
                GetServicesInfoRequest servicesInfoRequest = GetServicesInfoRequest.fromJson(payload);
                response = MonitoringDataRequestHandler.processRequest(servicesInfoRequest);
            } else if (request.getRequestType().equals("GetMetricHistoryRequest")) {
                GetMetricHistoryRequest getMetricHistoryRequest = GetMetricHistoryRequest.fromJson(payload);
                response = MonitoringDataRequestHandler.processRequest(getMetricHistoryRequest);
            } else if (request.getRequestType().equals("GetServiceViolationsFrequencyRequest")) {
                GetServiceViolationsFrequencyRequest serviceViolationsFrequencyReq = GetServiceViolationsFrequencyRequest.fromJson(payload);
                response = MonitoringDataRequestHandler.processRequest(serviceViolationsFrequencyReq);
            } else if (request.getRequestType().equals("GetServiceViolationsRequest")) {
                GetServiceViolationsRequest serviceViolationsReq = GetServiceViolationsRequest.fromJson(payload);
                response = MonitoringDataRequestHandler.processRequest(serviceViolationsReq);
            } else if (request.getRequestType().equals("GetServiceSLASummaryRequest")) {
                GetServiceSLASummaryRequest slaSummaryReq = GetServiceSLASummaryRequest.fromJson(payload);
                response = MonitoringDataRequestHandler.processRequest(slaSummaryReq);
            } else if (request.getRequestType().equals("GetServiceEventsRequest")) {
                GetServiceEventsRequest srvEventsReq = GetServiceEventsRequest.fromJson(payload);
                response = MonitoringDataRequestHandler.processRequest(srvEventsReq);
            } else if (request.getRequestType().equals("GetHostsInfoRequest")) {
                response = MonitoringDataRequestHandler.processRequest(request);
            } else if (request.getRequestType().equals("GetSchedulerEventsRequest")) {
                GetSchedulerEventsRequest schEventsReq = GetSchedulerEventsRequest.fromJson(payload);
                response = MonitoringDataRequestHandler.processRequest(schEventsReq);
            } else if (request.getRequestType().equals("GenerateReportRequest")) {
                GenerateReportRequest genReportRequest = GenerateReportRequest.fromJson(payload);
                response = MonitoringDataRequestHandler.processRequest(genReportRequest);
            } else {
                return;
            }
            String responsePayload = response.toJson();
            publishMessage(responsePayload);
            log.trace(String.format("Request %s processed successfully.", request.getMessageId()));
            if (log.isTraceEnabled()) {
                String text = response.toJson();
                log.trace("Response:\n" + text);
            }
        } catch (Exception e) {
            log.error(String.format("Error encountered while processing request %s: %s", Utils.shortenString(payload, 1000), e.getMessage()));
            ErrorResponse errorResponse = new ErrorResponse();
            String inReplyTo = "";
            if (request != null) {
                inReplyTo = request.getMessageId();
            }
            errorResponse.setInReplyTo(inReplyTo);
            errorResponse.setTimestamp(new Date());
            errorResponse.setExceptionMessage((e.getMessage() != null) ? e.getMessage() : e.toString());
            try {
                String responsePayload = errorResponse.toJson().toString();
                publishMessage(responsePayload);
            } catch (Exception e1) {
                log.error("Error encountered while sending error response: " + e.getMessage());
            }
        }
    }
