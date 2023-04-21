    protected void addCyclicFunction(String functionRef, double[] c0, double[] c1, double[] dom) throws IOException {
        PDFStream function = pdf.openStream(functionRef);
        function.entry("FunctionType", 4);
        function.entry("Domain", dom);
        function.entry("Range", new double[] { 0., 1., 0., 1., 0., 1. });
        function.println("{");
        for (int i = 0; i < 3; i++) {
            if (i < 2) function.println("dup");
            function.println((c1[i] - c0[i]) + " mul");
            function.println(c0[i] + " add");
            if (i < 2) function.println("exch");
        }
        function.println("}");
        pdf.close(function);
    }
