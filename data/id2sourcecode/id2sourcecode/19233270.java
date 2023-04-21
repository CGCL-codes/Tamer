    @SuppressWarnings("unchecked")
    private void addToOpsList(ServiceProxy serviceToAdd, HashMap<String, ArrayList<ServiceProxy>> opsList2, HashMap<String, String> methodNameToOperation) {
        MetadataSection meta = null;
        try {
            logger.log(Level.INFO, "Getting wsdl location from the service");
            meta = (MetadataSection) serviceToAdd.getServiceMetadata().getWsdls().get(0);
        } catch (DPWSException e1) {
            logger.log(Level.WARNING, "Error getting metadata for the service To add", e1);
            e1.printStackTrace();
        }
        try {
            logger.log(Level.INFO, "Setting up the url for adding to the ops list");
            URL url = new URL(meta.getLocation());
            logger.log(Level.INFO, "The URL for the service is" + meta.getLocation());
            StaxBuilder t = new StaxBuilder();
            InputFactory n = new InputFactory();
            XMLStreamReader reader = null;
            try {
                logger.log(Level.INFO, "Reading the stream from the url to the XMLStreamReader");
                reader = n.createXMLStreamReader(url.openStream());
            } catch (XMLStreamException e) {
                logger.log(Level.WARNING, "Error while trying to make an XMLStreamReader from url stream input", e);
                e.printStackTrace();
            }
            Document testDoc = null;
            try {
                logger.log(Level.INFO, "Trying to build the JDOM Document from the XMLStream");
                testDoc = t.build(reader);
            } catch (XMLStreamException e) {
                logger.log(Level.WARNING, "Error building the JDOM Document from the XMLStream", e);
                e.printStackTrace();
            }
            List contentList1 = testDoc.getContent();
            Element content1 = (Element) contentList1.get(0);
            logger.log(Level.INFO, "Filtering for portType in the Document");
            ElementFilter elFilter = new ElementFilter("portType");
            List filteredContent = content1.getContent(elFilter);
            Element portTypeElement = (Element) filteredContent.get(0);
            String portTypeName = portTypeElement.getAttributeValue("name");
            logger.log(Level.INFO, "Filtering for operations in the portType");
            ElementFilter opsFilter = new ElementFilter("operation");
            List filteredOps = portTypeElement.getContent(opsFilter);
            for (int i = 0; i < filteredOps.size(); i++) {
                Element opsElement = (Element) filteredOps.get(i);
                String opName = opsElement.getAttributeValue("name");
                logger.log(Level.INFO, "Filtering for input in the operations");
                ElementFilter inputFilter = new ElementFilter("input");
                List inputActionList = opsElement.getContent(inputFilter);
                if (inputActionList != null && inputActionList.size() > 0) {
                    logger.log(Level.INFO, "Operation has an input");
                    String totalRequestString = null;
                    Element requestElement = (Element) inputActionList.get(0);
                    String inputName = requestElement.getAttributeValue("message");
                    StringTokenizer tokenizer = new StringTokenizer(inputName, ":");
                    String prefix = tokenizer.nextToken();
                    String uri = null;
                    Iterator it = requestElement.getAdditionalNamespaces().iterator();
                    while (it.hasNext()) {
                        Namespace namespaceAttribute = (Namespace) it.next();
                        if (prefix.equals(namespaceAttribute.getPrefix())) {
                            uri = namespaceAttribute.getURI();
                            break;
                        }
                    }
                    logger.log(Level.INFO, "Filtering for output in the operation");
                    ElementFilter outputFilter = new ElementFilter("output");
                    List outputActionList = opsElement.getContent(outputFilter);
                    if (outputActionList != null && outputActionList.size() > 0) {
                        logger.log(Level.INFO, "Has Output");
                        totalRequestString = uri + portTypeName + "/" + tokenizer.nextToken();
                    } else {
                        logger.log(Level.INFO, "Does not have output");
                        totalRequestString = uri + portTypeName + "/" + opName;
                    }
                    String uniqueOpName = opName + "*" + serviceToAdd.getId();
                    methodNameToOperation.put(uniqueOpName, totalRequestString);
                    if (opsList2.get(opName) != null) {
                        opsList2.get(opName).add(serviceToAdd);
                    } else {
                        ArrayList<ServiceProxy> opsList = new ArrayList<ServiceProxy>();
                        opsList.add(serviceToAdd);
                        opsList2.put(opName, opsList);
                        if (opsListForService.get(serviceToAdd.getId()) == null) {
                            ArrayList<String> serviceList = new ArrayList<String>();
                            serviceList.add(opName);
                            opsListForService.put(serviceToAdd.getId(), serviceList);
                        } else {
                            opsListForService.get(serviceToAdd.getId()).add(opName);
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Error with the URL when adding operations to the operation list", e);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error with getting the stream from the URL", e);
        }
    }
