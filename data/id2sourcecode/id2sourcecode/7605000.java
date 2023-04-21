    public void run(IProcessStatus ps, Element input) {
        Element output = null;
        if (NS_RDF.equals(input.getNamespaceURI()) && "Description".equals(input.getLocalName())) {
            String resource = input.getAttribute("about");
            if (resource != null && resource.startsWith("http://rdf.openmolecules.net/?InChI=1/")) {
                System.out.println("resource: " + resource);
                try {
                    URL url = new URL(resource);
                    String rdfOutput = copyToString(url.openStream());
                    output = JavaDOMTools.string2Element(rdfOutput);
                } catch (Exception e) {
                    System.out.println("ERROR IN RDF.bridge.ron: creating element" + " failed: " + e.getMessage());
                    e.printStackTrace();
                    output = null;
                }
            } else {
                ps.setError("Expected the rdf:about value to start with " + "http://rdf.openmolecules.net/?InChI=1/, but got: " + resource);
            }
        } else {
            ps.setError("Expected rdf:Description but got: " + input.getNamespaceURI());
        }
        ps.setResult(output, "Done");
    }
