    private void defineDeviceNodes(Vector<String> handles, AcceleratorNode dev) {
        for (int k = 0; k < handles.size(); k++) {
            Channel channel = dev.getChannel(handles.elementAt(k));
            if (channel != null) {
                HandleNode h_node = new HandleNode(handles.elementAt(k));
                h_node.setAsSignal(true);
                h_node.setChannel(channel);
                h_node.setSignalName(dev.getChannel(handles.elementAt(k)).getId());
                add(h_node);
            }
        }
    }
