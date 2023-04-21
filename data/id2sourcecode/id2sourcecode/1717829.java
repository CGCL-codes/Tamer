    @SuppressWarnings("unchecked")
    protected OutMessage processEPR(DPWSContextImpl context, EndpointReferenceElement epr, OperationInfo aoi, AbstractAddressingHeaders inHeaders, AddressingHeadersFactory200408 factory) throws DPWSFault, DPWSException {
        String addr = epr.getAddress();
        OutMessage outMessage = null;
        String messId = inHeaders.messageID;
        boolean isFault = epr.getName().equals(WSAConstants.WSA_FAULT_TO);
        Transport t = null;
        if (addr == null) {
            throw new DPWSFault("Invalid ReplyTo address.", DPWSFault.SENDER);
        }
        if (addr.equals(factory.getAnonymousUri())) {
            outMessage = new OutMessage(Channel.BACKCHANNEL_URI);
            t = context.getExchange().getInMessage().getChannel().getTransport();
        } else if (isNoneAddress(factory, addr)) {
            t = new DeadLetterTransport();
            outMessage = new OutMessage(addr);
        } else {
            outMessage = new OutMessage(addr);
            if (!isFault) sendEmptyResponse(context);
            t = context.getDpws().getTransportManager().getTransportForUri(addr);
        }
        outMessage.setSoapVersion(context.getExchange().getInMessage().getSoapVersion());
        if (t == null) {
            throw new DPWSFault("URL was not recognized: " + addr, DPWSFault.SENDER);
        }
        outMessage.setChannel(t.createChannel());
        AddressingHeaders headers = new AddressingHeaders(null, null);
        if (!isFault) {
            headers.to = addr;
            headers.action = aoi.getOutAction();
            headers.relatesTo = messId;
        } else {
            headers.action = WSAConstants.WSA_200408_FAULT_ACTION;
            headers.relatesTo = messId;
            headers.relationshipType = faultQName;
        }
        Element refParam = epr.getReferenceParametersElement();
        if (refParam != null) {
            List<Element> refs = refParam.cloneContent();
            List<Element> params = new ArrayList<Element>();
            for (int i = 0; i < refs.size(); i++) {
                if (refs.get(i) != null) {
                    Element e = refs.get(i);
                    e.setAttribute(new Attribute(WSAConstants.WSA_IS_REF_PARAMETER, "true", epr.getNamespace()));
                    params.add(e);
                }
            }
            headers.referenceParameters = params;
        }
        outMessage.setProperty(ADRESSING_HEADERS, headers);
        outMessage.setProperty(ADRESSING_FACTORY, factory);
        return outMessage;
    }
