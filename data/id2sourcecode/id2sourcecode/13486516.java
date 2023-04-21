    private String getInstructions() throws Exception {
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        SAXBuilder parser = new SAXBuilder();
        parser.setValidation(false);
        String instructionURL = this.getSubmissionInstructionsUrl();
        if (instructionURL == null) {
            return null;
        }
        Document instructionDoc = null;
        if (instructionURL.toLowerCase().startsWith("http")) {
            try {
                URL url = new URL(instructionURL);
                instructionDoc = parser.build(url.openStream());
            } catch (Exception e) {
                log.warn("Unable to parse submission instructions " + instructionURL, e);
            }
        } else {
            instructionDoc = parser.build(new FileInputStream(GNomExFrontController.getWebContextPath() + instructionURL));
        }
        if (instructionDoc != null) {
            Document tempDoc = new Document(new Element("div"));
            for (Iterator i = instructionDoc.getRootElement().getChildren("body").iterator(); i.hasNext(); ) {
                Element body = (Element) i.next();
                for (Iterator i1 = body.getChildren().iterator(); i1.hasNext(); ) {
                    Element node = (Element) i1.next();
                    tempDoc.getRootElement().addContent((Element) node.clone());
                }
            }
            return out.outputString(tempDoc.getRootElement());
        } else {
            return null;
        }
    }
