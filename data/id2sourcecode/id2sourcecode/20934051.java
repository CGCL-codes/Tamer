    protected final Class[] getOutputRecordDatatypes(String pChannel) throws ClassNotFoundException, KETLThreadException {
        Class[] result = (Class[]) this.mChannelClassMapping.get(pChannel);
        if (result == null) {
            String[] channels = ETLWorker.getChannels(this.getXMLConfig());
            for (String element : channels) {
                if (element.equals(pChannel)) {
                    Node[] nList = XMLHelper.getElementsByName(this.getXMLConfig(), "OUT", "CHANNEL", element);
                    ArrayList al = new ArrayList();
                    int portIndex = 0;
                    for (Node element0 : nList) {
                        if (this.portUsed(pChannel, ((Element) element0).getAttribute("NAME"))) {
                            ETLOutPort port = (this.getOutPort(((Element) element0).getAttribute("NAME")));
                            al.add(port.getPortClass());
                            port.setIndex(portIndex);
                            this.mHmOutportIndex.put(port, portIndex++);
                        }
                    }
                    Class[] cls = new Class[al.size()];
                    al.toArray(cls);
                    this.mChannelClassMapping.put(element, cls);
                    this.setOutputRecordDataTypes(cls, pChannel);
                    return cls;
                }
            }
        } else return result;
        throw new KETLThreadException("Invalid channel request", this);
    }
