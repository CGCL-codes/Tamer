    private boolean loadWsdlFromDir(String wsdlName, ServiceEndpoint ServiceInst, Bundle bundle, String wsdlDir) {
        if (bundle.findEntries(wsdlDir, wsdlName, true) != null) {
            System.out.println("WSDL detected");
            for (Enumeration<URL> wsdlobjects = bundle.findEntries(wsdlDir, wsdlName, true); wsdlobjects.hasMoreElements(); ) {
                try {
                    URL url = wsdlobjects.nextElement();
                    if (url == null) break;
                    InputStream input = (url).openStream();
                    byte[] WSDL = new byte[input.available()];
                    input.read(WSDL);
                    ServiceInst.setProperty("WSDL", WSDL);
                    return true;
                } catch (IOException e) {
                    System.out.println("Cannot read wsdl: " + e);
                    break;
                }
            }
        }
        return false;
    }
