    public static void main(String args[]) throws Exception {
        ProtocolStack protocolStack = new ProtocolStack();
        protocolStack.insertLayer(new OutInLayer());
        protocolStack.insertLayer(new IncrementLayer());
        protocolStack.insertLayer(new CheckErrorLayer());
        protocolStack.insertLayer(new ProtocolLayerEndPoint(new IMCUnMarshaler(System.in), new IMCMarshaler(System.out)));
        String readline;
        UnMarshaler unMarshaler;
        Marshaler marshaler;
        do {
            unMarshaler = protocolStack.createUnMarshaler();
            readline = unMarshaler.readStringLine();
            marshaler = protocolStack.createMarshaler();
            marshaler.writeStringLine("read:\n" + readline);
            protocolStack.releaseMarshaler(marshaler);
        } while (true);
    }
