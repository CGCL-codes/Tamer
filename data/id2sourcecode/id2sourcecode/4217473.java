    private static void doTransformation(String infile, String outfile) {
        System.out.println("Will read " + infile + " and write " + outfile);
        String ext = infile.substring(infile.lastIndexOf('.') + 1);
        if (ext.equals("xml")) {
            System.out.println("Converting SBML");
            SBMLtoJWS s2j = new SBMLtoJWS();
            if (!s2j.extractWriteModel(infile, outfile)) {
                System.out.println("Failed to extract SBML and write JWS format.");
            }
        } else if (ext.equals("dat")) {
            try {
                JWStoSBML j2s = new JWStoSBML();
                if (!j2s.extractWriteModel(infile, outfile)) {
                    System.out.println("Failed to extract JWS and write SBML format.");
                }
            } catch (Exception e) {
                System.out.println("Caught exception converting JWS to SBML: " + e.getMessage());
            }
        } else {
            System.err.println("Error: wrong extension!");
            System.exit(2);
        }
    }
