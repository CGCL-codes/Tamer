    private void writeAddBindings00Method(PrintWriter writer) throws IOException {
        java.io.InputStream input = this.getClass().getClassLoader().getResourceAsStream("communication/customFacadeSoapBindingStub.java.txt");
        BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(input));
        writeTail(reader, writer);
        reader.close();
    }
