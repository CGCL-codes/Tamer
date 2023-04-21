    static void rdfOneFile(Reader reader, PrintWriter writer, String baseName, String filename) {
        try {
            Model model = ModelFactory.createDefaultModel();
            model.read(reader, baseName, "N3");
            if (printRDF) {
                if (outputLang.equals("N3")) {
                    writer.print("# Jena N3->RDF->" + outputLang + " : " + filename);
                    writer.println();
                    writer.println();
                }
                model.write(writer, outputLang, baseName);
                writer.flush();
            }
        } catch (JenaException rdfEx) {
            Throwable cause = rdfEx.getCause();
            N3Exception n3Ex = (N3Exception) (rdfEx instanceof N3Exception ? rdfEx : cause instanceof N3Exception ? cause : null);
            if (n3Ex != null) System.err.println(n3Ex.getMessage()); else {
                Throwable th = (cause == null ? rdfEx : cause);
                System.err.println(th.getMessage());
                th.printStackTrace(System.err);
            }
            System.exit(7);
        }
    }
