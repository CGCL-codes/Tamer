    public RegistryObject execute(RegistryObject uddiRequest) throws RegistryException {
        URL endPointURL = null;
        if (uddiRequest instanceof Inquiry) endPointURL = Config.getInquiryURL(); else if (uddiRequest instanceof Publish || uddiRequest instanceof SecurityPolicy) endPointURL = Config.getPublishURL(); else if (uddiRequest instanceof Admin) endPointURL = Config.getAdminURL(); else throw new RegistryException("Unsupported Request: The " + "request '" + uddiRequest.getClass().getName() + "' is an " + "invalid or unknown request type.");
        Document document = XMLUtils.createDocument();
        Element temp = document.createElement("temp");
        String requestName = uddiRequest.getClass().getName();
        IHandler requestHandler = maker.lookup(requestName);
        requestHandler.marshal(uddiRequest, temp);
        Element request = (Element) temp.getFirstChild();
        request.setAttribute("generic", Config.getStringProperty("juddi.clientGeneric", Registry.UDDI_V2_GENERIC));
        request.setAttribute("xmlns", Config.getStringProperty("juddi.clientXMLNS", Registry.UDDI_V2_NAMESPACE));
        Element response = transport.send(request, endPointURL);
        String responseName = response.getLocalName();
        if (responseName == null) {
            throw new RegistryException("Unsupported response " + "from registry. A value was not present.");
        }
        IHandler handler = maker.lookup(responseName.toLowerCase());
        if (handler == null) {
            throw new RegistryException("Unsupported response " + "from registry. Response type '" + responseName + "' is unknown.");
        }
        RegistryObject uddiResponse = handler.unmarshal(response);
        if (uddiResponse instanceof RegistryException) throw ((RegistryException) uddiResponse);
        return uddiResponse;
    }
