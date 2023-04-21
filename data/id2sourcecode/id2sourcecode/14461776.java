    public void close() throws OneWireException, OneWireIOException {
        OWPathElement path_element;
        SwitchContainer sw;
        byte[] sw_state;
        for (int i = elements.size() - 1; i >= 0; i--) {
            path_element = (OWPathElement) elements.elementAt(i);
            sw = (SwitchContainer) path_element.getContainer();
            sw_state = sw.readDevice();
            sw.setLatchState(path_element.getChannel(), false, false, sw_state);
            sw.writeDevice(sw_state);
        }
    }
