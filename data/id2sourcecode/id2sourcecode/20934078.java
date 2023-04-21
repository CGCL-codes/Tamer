    public final ETLOutPort setOutUsed(String pChannel, String pPort) throws KETLThreadException {
        if (this.getOutPort(pPort) == null) throw new KETLThreadException("Invalid port name " + this.mstrName + "." + pPort, this);
        if (pChannel == null) {
            pChannel = ETLWorker.getChannels(this.getXMLConfig())[ETLWorker.DEFAULT];
        }
        ETLOutPort port = this.hmOutports.get(pPort);
        HashSet al = (HashSet) this.mChannelPortsUsed.get(pChannel);
        if (al == null) {
            al = new HashSet();
            this.mChannelPortsUsed.put(pChannel, al);
        }
        al.add(pPort);
        port.used(true);
        if (port.getPortClass() == null) {
            try {
                port.setDataTypeFromPort(port.getAssociatedInPort());
            } catch (ClassNotFoundException e) {
                throw new KETLThreadException(e, this);
            }
        }
        return port;
    }
