    public Hashtable generateWSDL(String serviceLocation, QName wsdlQName_, String security, ServiceMapType serviceMap, boolean abstractWSDL) throws GFacSchemaException {
        Hashtable table = new Hashtable();
        QName wsdlQName = null;
        String wsdlString = null;
        String serviceName = serviceMap.getService().getServiceName().getStringValue();
        String nameSpaceURI = serviceMap.getService().getServiceName().getTargetNamespace();
        QName serviceQName = new QName(nameSpaceURI, serviceName);
        if (wsdlQName_ != null && !abstractWSDL) {
            wsdlQName = wsdlQName_;
        } else if (wsdlQName_ == null && !abstractWSDL) {
            String date = (new Date()).toString();
            date = date.replaceAll(" ", "_");
            date = date.replaceAll(":", "_");
            Random rand = new Random();
            int rdint = rand.nextInt(1000000);
            wsdlQName = new QName(nameSpaceURI, serviceName + "_" + date + "_" + rdint);
        }
        PortTypeType[] portTypes = serviceMap.getPortTypeArray();
        MethodType[] methods = portTypes[0].getMethodArray();
        QName portTypeName = serviceQName;
        String portName = portTypeName.getLocalPart();
        try {
            WSDLFactory fac = WSDLFactory.newInstance();
            WSDLWriter wsWriter = fac.newWSDLWriter();
            Definition def = fac.newDefinition();
            String typens = nameSpaceURI + "/" + portName + "/" + "xsd";
            String globalTypens = nameSpaceURI + "/" + "xsd";
            if (abstractWSDL) {
                def.setQName(serviceQName);
                logger.info("Service QName set to = " + serviceQName);
            } else {
                def.setQName(wsdlQName);
                logger.info("WSDL QName set to = " + wsdlQName);
            }
            def.setTargetNamespace(nameSpaceURI);
            def.addNamespace(WSDLNS, nameSpaceURI);
            def.addNamespace(TYPENS, typens);
            def.addNamespace(GLOBAL_TYPENS, globalTypens);
            def.addNamespace(SOAP, SOAP_NAMESPACE);
            def.addNamespace(XSD, XSD_NAMESPACE);
            def.addNamespace(CROSSCUT_PREFIX, "http://lead.extreme.indiana.edu/namespaces/2006/lead-crosscut-parameters/");
            def.addNamespace(WSA_PREFIX, "http://www.w3.org/2005/08/addressing");
            if (GFacConstants.TRANSPORT_LEVEL.equals(security) || GFacConstants.MESSAGE_SIGNATURE.equals(security)) {
                def.addNamespace(WSA_PREFIX, WSA_NAMESPACE);
                def.addNamespace("wsp", WSP_NAMESPACE);
                def.addNamespace("wsu", WSU_NAMESPACE);
                def.addNamespace("wspe", WSPE_NAMESPACE);
                def.addNamespace("sp", SP_NAMESPACE);
                def.addNamespace("wss10", WSS10_NAMESPACE);
                def.addNamespace("sp", SP_NAMESPACE);
                def.addNamespace("wst", WST_NAMESPACE);
            }
            javax.xml.parsers.DocumentBuilderFactory domfactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder builder = null;
            try {
                builder = domfactory.newDocumentBuilder();
            } catch (javax.xml.parsers.ParserConfigurationException e) {
                throw new GFacSchemaException("Parser configuration exception: " + e.getMessage());
            }
            DOMImplementation dImpl = builder.getDOMImplementation();
            String policyID = portName + "_Policy";
            String inputPolicyID = portName + "_operationPolicy";
            UnknownExtensibilityElement serviceLevelPolicRef = null;
            UnknownExtensibilityElement opLevelPolicRef = null;
            String namespace = GFacConstants.GFAC_NAMESPACE;
            Document doc = dImpl.createDocument(namespace, "factoryServices", null);
            String description = serviceMap.getService().getServiceDescription();
            if (description != null) {
                Element documentation = doc.createElementNS("http://schemas.xmlsoap.org/wsdl/", "wsdl:documentation");
                documentation.appendChild(doc.createTextNode(description));
                def.setDocumentationElement(documentation);
            }
            if (GFacConstants.TRANSPORT_LEVEL.equals(security)) {
                def.addExtensibilityElement(createTransportLevelPolicy(dImpl, policyID));
                serviceLevelPolicRef = createWSPolicyRef(dImpl, policyID);
            } else if (GFacConstants.MESSAGE_SIGNATURE.equals(security)) {
                def.addExtensibilityElement(WSPolicyGenerator.createServiceLevelPolicy(dImpl, policyID));
                def.addExtensibilityElement(WSPolicyGenerator.createOperationLevelPolicy(dImpl, "inputPolicy", inputPolicyID));
                serviceLevelPolicRef = createWSPolicyRef(dImpl, policyID);
                opLevelPolicRef = createWSPolicyRef(dImpl, inputPolicyID);
            }
            Types types = TypesGenerator.addTypes(def, dImpl, serviceMap, typens, globalTypens, methods);
            def.setTypes(types);
            PortTypeImpl portType = addPortTypes(def, dImpl, portTypes, serviceQName);
            Binding binding = addBinding(def, nameSpaceURI, portType, serviceLevelPolicRef, dImpl);
            Vector methodDesc = WSDLGenUtil.getMethodDescriptions(serviceMap);
            for (int i = 0; i < methods.length; ++i) {
                String methodName = methods[i].getMethodName();
                Vector outParams = WSDLGenUtil.getOutParams(serviceMap, methodName);
                OperationImpl operation = addOperation(def, dImpl, methodName, (String) methodDesc.get(i), typens, outParams);
                portType.addOperation(operation);
                if (!abstractWSDL) {
                    UnknownExtensibilityElement wsInPolicyRef = null;
                    UnknownExtensibilityElement wsOutPolicyRef = null;
                    BindingInputImpl bindingInput = addBindingInput(def, methodName, wsInPolicyRef);
                    BindingOutputImpl bindingOutput = addBindingOutput(def, methodName, outParams, wsOutPolicyRef);
                    BindingOperation bindingOperation = addBindingOperation(def, operation, dImpl);
                    bindingOperation.setBindingInput(bindingInput);
                    bindingOperation.setBindingOutput(bindingOutput);
                    binding.addBindingOperation(bindingOperation);
                    if (opLevelPolicRef != null) {
                        binding.addExtensibilityElement(opLevelPolicRef);
                    }
                }
            }
            def.addPortType(portType);
            if (!abstractWSDL) {
                def.addBinding(binding);
                ServiceImpl service = (ServiceImpl) def.createService();
                service.setQName(wsdlQName);
                PortImpl port = (PortImpl) def.createPort();
                port.setName(wsdlQName.getLocalPart() + WSDL_PORT_SUFFIX);
                port.setBinding(binding);
                service.addPort(port);
                SOAPAddressImpl soapAddress = new SOAPAddressImpl();
                soapAddress.setLocationURI(serviceLocation);
                port.addExtensibilityElement(soapAddress);
                def.addService(service);
            }
            if (!abstractWSDL) {
                table.put(WSDL_QNAME, wsdlQName);
            }
            table.put(SERVICE_QNAME, serviceQName);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            wsWriter.writeWSDL(def, bs);
            wsdlString = bs.toString();
        } catch (WSDLException e) {
            throw new GFacSchemaException("Error generating WSDL: " + e.getMessage());
        }
        Reader reader = new StringReader(wsdlString);
        Writer writer = new StringWriter();
        try {
            RoundTrip.roundTrip(reader, writer, "  ");
        } catch (XmlPullParserException e) {
            throw new GFacSchemaException(e);
        } catch (IOException e) {
            throw new GFacSchemaException(e);
        }
        wsdlString = writer.toString();
        if (abstractWSDL) {
            table.put(AWSDL, wsdlString);
        } else {
            table.put(WSDL, wsdlString);
        }
        return table;
    }
