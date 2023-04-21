    protected final void registerUsedPorts(ETLWorker pWorker, Node[] nl, String objectNameInCode) throws KETLThreadException {
        Node wildCardPort = null;
        Node wildCardOut = null;
        ETLWorker duplicateSource = (ETLWorker) this.mFanInWorkerUsed.get(pWorker.mstrName);
        if (duplicateSource == null) {
            Node[] outNodes = XMLHelper.getElementsByName(this.getXMLConfig(), "OUT", "*", "*");
            HashSet outExists = new HashSet();
            if (outNodes != null) {
                for (Node element : outNodes) {
                    String content = XMLHelper.getTextContent(element);
                    if (content != null && content.trim().equals("*")) {
                        wildCardOut = element;
                        this.getXMLConfig().removeChild(wildCardOut);
                    } else {
                        String portName = XMLHelper.getAttributeAsString(element.getAttributes(), "NAME", null);
                        if (portName == null && content != null) {
                            content = content.trim();
                            if (content.startsWith(EngineConstants.VARIABLE_PARAMETER_START) && content.endsWith(EngineConstants.VARIABLE_PARAMETER_END)) {
                                String tmp[] = EngineConstants.getParametersFromText(content);
                                if (tmp != null && tmp.length == 1) {
                                    portName = tmp[0];
                                } else portName = null;
                            }
                        }
                        if (portName != null) outExists.add(portName);
                    }
                }
            }
            HashSet srcPortsUsed = new HashSet();
            for (Node node : nl) {
                ETLOutPort srcPort = null;
                ETLInPort newPort = this.getNewInPort((ETLStep) pWorker);
                newPort.setCodeGenerationReferenceObject(objectNameInCode);
                if (ETLPort.containsConstant(XMLHelper.getTextContent(node)) == false) {
                    String[] sources = ETLWorker.extractPortDetails(XMLHelper.getTextContent(node));
                    if (sources == null) continue;
                    ResourcePool.LogMessage(this, ResourcePool.DEBUG_MESSAGE, "getUsedPortsFromWorker -> Port: " + ((Element) node).getAttribute("NAME") + " ");
                    if (sources[ETLWorker.PORT].equals("*")) {
                        if (wildCardPort != null) throw new KETLThreadException("Duplicate wild card IN port exists, check step " + this.getName() + " port XML -> " + XMLHelper.outputXML(node), this);
                        wildCardPort = node;
                        newPort = null;
                    } else {
                        srcPort = pWorker.setOutUsed(sources[ETLWorker.CHANNEL], sources[ETLWorker.PORT]);
                        srcPortsUsed.add(srcPort);
                        newPort.setSourcePort(srcPort);
                        ArrayList res = (ArrayList) this.mhmInportIndex.get(objectNameInCode);
                        if (res == null) {
                            res = new ArrayList();
                            this.mhmInportIndex.put(objectNameInCode, res);
                        }
                        res.add(newPort);
                    }
                } else {
                    ArrayList res = (ArrayList) this.mhmInportIndex.get(objectNameInCode);
                    if (res == null) {
                        res = new ArrayList();
                        this.mhmInportIndex.put(objectNameInCode, res);
                    }
                    res.add(newPort);
                }
                if (newPort != null) {
                    try {
                        newPort.initialize(node);
                        if (srcPort != null) newPort.setDataTypeFromPort(srcPort);
                    } catch (Exception e) {
                        throw new KETLThreadException(e, this);
                    }
                    if (this.hmInports.put(newPort.mstrName, newPort) != null) throw new KETLThreadException("Duplicate IN port name exists, check step " + this.getName() + " port " + newPort.mstrName, this);
                }
            }
            if (wildCardPort != null) {
                ArrayList otherPorts = new ArrayList();
                pWorker.initializeAllOutPorts();
                String[] sources = ETLWorker.extractPortDetails(XMLHelper.getTextContent(wildCardPort));
                Node parent = wildCardPort.getParentNode();
                String channel = (sources.length == 3 ? sources[ETLWorker.CHANNEL] : null);
                parent.removeChild(wildCardPort);
                NamedNodeMap nm = wildCardPort.getAttributes();
                for (ETLOutPort src : pWorker.getOutPorts()) {
                    if (channel != null && src.getChannel().equals(channel) == false) continue;
                    if (srcPortsUsed.contains(src)) continue;
                    ETLPort ePort = (ETLPort) this.hmInports.get(src.mstrName);
                    if (ePort != null && ePort.isConstant()) continue;
                    if (ePort != null) {
                        throw new KETLThreadException("IN port already exists from another source with the same name check step " + this.getName() + " port " + src.mstrName, this);
                    }
                    Element e = parent.getOwnerDocument().createElement("IN");
                    for (int i = 0; i < nm.getLength(); i++) {
                        Node n = nm.item(i);
                        if (n instanceof Attr) e.setAttribute(((Attr) n).getName(), ((Attr) n).getValue());
                    }
                    e.setAttribute("NAME", src.mstrName);
                    e.setTextContent(pWorker.mstrName + "." + src.getChannel() + "." + src.mstrName);
                    parent.appendChild(e);
                    otherPorts.add(e);
                }
                if (otherPorts.size() > 0) {
                    nl = new Node[otherPorts.size()];
                    otherPorts.toArray(nl);
                    this.registerUsedPorts(pWorker, nl, objectNameInCode);
                }
            }
            if (wildCardOut != null) {
                NamedNodeMap nm = wildCardOut.getAttributes();
                for (Object o : this.hmInports.values()) {
                    ETLInPort export = (ETLInPort) o;
                    if (outExists.contains(export.mstrName)) continue;
                    Element e = this.getXMLConfig().getOwnerDocument().createElement("OUT");
                    for (int i = 0; i < nm.getLength(); i++) {
                        Node n = nm.item(i);
                        if (n instanceof Attr && ((Attr) n).getName().equals("DATATYPE") == false) e.setAttribute(((Attr) n).getName(), ((Attr) n).getValue());
                    }
                    e.setAttribute("NAME", export.mstrName);
                    e.setTextContent(EngineConstants.VARIABLE_PARAMETER_START + export.mstrName + EngineConstants.VARIABLE_PARAMETER_END);
                    this.getXMLConfig().appendChild(e);
                }
            }
            this.mFanInWorkerUsed.put(pWorker.mstrName, pWorker);
        } else {
            for (ETLOutPort p : duplicateSource.getOutPorts()) {
                if (p.isUsed()) pWorker.setOutUsed(p.getChannel(), p.mstrName);
            }
            pWorker.initializeAllOutPorts();
        }
    }
