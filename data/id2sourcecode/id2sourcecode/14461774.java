    public String toString() {
        StringBuffer sb = new StringBuffer();
        try {
            sb.append(adapter.getAdapterName()).append("_").append(adapter.getPortName()).append("/");
        } catch (OneWireException e) {
            sb.append(adapter.getAdapterName()).append("/");
        }
        for (int i = 0; i < elements.size(); i++) {
            OWPathElement element = (OWPathElement) elements.elementAt(i);
            OneWireContainer owc = element.getContainer();
            sb.append(owc.getAddressAsString()).append("_").append(element.getChannel()).append("/");
        }
        return sb.toString();
    }
