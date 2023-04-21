    public void generate() throws Wsdl2JsException, IOException {
        if (inputFile == null) throw new Wsdl2JsException("No WSDL input file specified");
        if (outputFile == null) throw new Wsdl2JsException("No JS output file specified");
        if (!inputFile.exists()) throw new Wsdl2JsException("WSDL input file doesn't exist");
        Definition def = null;
        try {
            WSDLFactory wf = WSDLFactory.newInstance();
            WSDLReader wr = wf.newWSDLReader();
            wr.setFeature("javax.wsdl.verbose", false);
            wr.setFeature("javax.wsdl.importDocuments", true);
            InputSource inSrc = new InputSource(new FileInputStream(inputFile));
            def = wr.readWSDL(inputFile.getParentFile().toURI().toString(), inSrc);
        } catch (Exception x) {
            throw new Wsdl2JsException("Error reading WSDL from: " + inputFile.getAbsolutePath(), x);
        }
        typeInfoMap = new HashMap<QName, String>();
        popInfoList = new ArrayList<String>();
        typeInfoList = new ArrayList<String>();
        readSchema(def.getTypes());
        Iterator<?> srvIt = def.getServices().values().iterator();
        if (srvIt.hasNext()) {
            Service service = (Service) srvIt.next();
            Iterator<?> prtIt = service.getPorts().values().iterator();
            if (prtIt.hasNext()) {
                Port port = (Port) prtIt.next();
                String stubClass = Constants.STUBGEN_DEFAULT_JSNAMESPACE + service.getQName().getLocalPart();
                JsParam[] constParams = new JsParam[] { new JsParam("cbObj") };
                JsClass jsClass = new JsClass(stubClass, "SOAP_Stub", constParams);
                JsBlock block = jsClass.getConstructorBody();
                block.addStatement(new JsStatement("this._cbObj=cbObj"));
                block.addStatement(new JsStatement("this._setService(\"" + service.getQName().getLocalPart() + "\")"));
                block.addStatement(new JsStatement("this._setRequestPath(\"###REQUESTPATH###\")"));
                Binding binding = port.getBinding();
                Iterator<?> bopIt = binding.getBindingOperations().iterator();
                while (bopIt.hasNext()) {
                    BindingOperation bop = (BindingOperation) bopIt.next();
                    Operation op = bop.getOperation();
                    try {
                        JsMethod jsMethod = new JsMethod(jsClass, op.getName());
                        JsBlock jsBlock = jsMethod.getBody();
                        jsBlock.addStatement(new JsStatement("var call=this._createCall()"));
                        jsBlock.addStatement(new JsStatement("call.setTargetNamespace(this._targetNamespace)"));
                        jsBlock.addStatement(new JsStatement("call.setOperationName(\"" + jsMethod.getName() + "\")"));
                        Input input = op.getInput();
                        Message inputMsg = input.getMessage();
                        Part inputPart = inputMsg.getPart("parameters");
                        if (inputPart != null) {
                            QName typeName = schemaElementsToTypes.get(inputPart.getElementName());
                            Element typeElem = schemaComplexTypes.get(typeName);
                            Element[] children = getSchemaChildren(typeElem);
                            if (children.length > 1 || (children.length == 1 && !children[0].getLocalName().equals("sequence"))) throw new Wsdl2JsException("Expected only single sequence child element for complexType: " + typeName);
                            children = getSchemaChildren(children[0]);
                            for (int paraNo = 0; paraNo < children.length; paraNo++) {
                                if (!children[paraNo].getLocalName().equals("element")) throw new Wsdl2JsException("Expected only element children within sequence of complexType: " + typeName);
                                String name = children[paraNo].getAttribute("name");
                                JsParam jsParam = new JsParam(name);
                                jsMethod.addParam(jsParam);
                                String info = createTypeInfo(children[paraNo]);
                                jsBlock.addStatement(new JsStatement("call.addParameter(\"" + name + "\"," + info + ")"));
                            }
                        }
                        jsBlock.addStatement(new JsStatement("this._extractCallback(call,arguments," + jsMethod.getParams().length + ")"));
                        Output output = op.getOutput();
                        Message outputMsg = output.getMessage();
                        Part outputPart = outputMsg.getPart("parameters");
                        if (outputPart != null) {
                            QName typeName = schemaElementsToTypes.get(outputPart.getElementName());
                            Element typeElem = schemaComplexTypes.get(typeName);
                            Element[] children = getSchemaChildren(typeElem);
                            if (children.length > 1 || (children.length == 1 && !children[0].getLocalName().equals("sequence"))) throw new Wsdl2JsException("Expected only single sequence child element for complexType: " + typeName);
                            children = getSchemaChildren(children[0]);
                            if (children.length > 1) throw new Wsdl2JsException("Expected only one or none return parameters");
                            if (children.length == 1) {
                                String info = createTypeInfo(children[0]);
                                jsBlock.addStatement(new JsStatement("call.setReturnType(" + info + ")"));
                            }
                        }
                        jsBlock.addStatement(new JsStatement("return call.invoke(" + jsMethod.getParamList() + ")"));
                        jsClass.addMethod(jsMethod);
                    } catch (Wsdl2JsTypeException x) {
                        System.out.println("WARNING: Skip method '" + op.getName() + "' as signature " + "contains unsupported type '" + x.getTypeName() + "'.");
                    }
                }
                block.addStatement(new JsStatement("this._targetNamespace=\"" + def.getTargetNamespace() + "\""));
                for (int i = 0; i < typeInfoList.size(); i++) {
                    block.addStatement(new JsStatement("this._typeInfos[" + i + "]=" + (String) typeInfoList.get(i)));
                }
                for (int i = 0; i < popInfoList.size(); i++) {
                    block.addStatement(new JsStatement((String) popInfoList.get(i)));
                }
                jsClass.printCode(new FileOutputStream(outputFile));
            }
        }
    }
