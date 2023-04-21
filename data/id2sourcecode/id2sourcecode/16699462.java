    public static void main(String args[]) throws Exception {
        ProtocolLayer protocolLayer = new ProtocolLayerEndPoint(new IMCUnMarshaler(System.in), new IMCMarshaler(System.out));
        String readline;
        UnMarshaler unMarshaler;
        Marshaler marshaler;
        do {
            unMarshaler = protocolLayer.doCreateUnMarshaler(null);
            readline = unMarshaler.readStringLine();
            marshaler = protocolLayer.doCreateMarshaler(null);
            marshaler.writeStringLine("input: " + readline);
            protocolLayer.doReleaseMarshaler(marshaler);
        } while (true);
    }
