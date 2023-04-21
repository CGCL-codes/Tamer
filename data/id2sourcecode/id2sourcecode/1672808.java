    private void addLinearFunction(String functionRef, double[] c0, double[] c1, double[] dom) throws IOException {
        PDFDictionary function = pdf.openDictionary(functionRef);
        function.entry("FunctionType", 2);
        function.entry("Domain", dom);
        function.entry("Range", new double[] { 0., 1., 0., 1., 0., 1. });
        function.entry("C0", c0);
        function.entry("C1", c1);
        function.entry("N", 1);
        pdf.close(function);
    }
