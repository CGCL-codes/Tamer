    public void open() throws OneWireException, OneWireIOException {
        OWPathElement path_element;
        SwitchContainer sw;
        byte[] sw_state;
        for (int i = 0; i < elements.size(); i++) {
            path_element = (OWPathElement) elements.elementAt(i);
            sw = (SwitchContainer) path_element.getContainer();
            sw_state = sw.readDevice();
            sw.setLatchState(path_element.getChannel(), true, sw.hasSmartOn(), sw_state);
            sw.writeDevice(sw_state);
        }
        if (elements.size() == 0) {
            adapter.reset();
        }
    }
