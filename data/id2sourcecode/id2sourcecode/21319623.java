    public static void main(String[] argv) throws WSDLException {
        if (argv.length == 1) {
            WSDLFactory wsdlFactory = WSDLFactory.newInstance();
            WSDLReader wsdlReader = wsdlFactory.newWSDLReader();
            WSDLWriter wsdlWriter = wsdlFactory.newWSDLWriter();
            wsdlWriter.writeWSDL(wsdlReader.readWSDL(null, argv[0]), System.out);
        } else {
            System.err.println("Usage:");
            System.err.println();
            System.err.println("  java " + WSDLWriterImpl.class.getName() + " filename|URL");
            System.err.println();
            System.err.println("This test driver simply reads a WSDL document " + "into a model (using a WSDLReader), and then " + "serializes it back to standard out. In effect, " + "it performs a round-trip test on the specified " + "WSDL document.");
        }
    }
