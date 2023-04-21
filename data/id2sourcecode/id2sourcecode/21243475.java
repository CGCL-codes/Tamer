    private void checkAcceptableUri(XPathContext context, Result result) throws XPathException {
        Controller controller = context.getController();
        String uri = result.getSystemId();
        if (uri != null) {
            if (controller.getDocumentPool().find(uri) != null) {
                XPathException err = new XPathException("Cannot write to a URI that has already been read: " + result.getSystemId());
                err.setXPathContext(context);
                err.setLocator(this);
                err.setErrorCode("XTRE1500");
                throw err;
            }
            DocumentURI documentKey = new DocumentURI(uri);
            if (!controller.checkUniqueOutputDestination(documentKey)) {
                XPathException err = new XPathException("Cannot write more than one result document to the same URI: " + result.getSystemId());
                err.setXPathContext(context);
                err.setLocator(this);
                err.setErrorCode("XTDE1490");
                throw err;
            } else {
                controller.addUnavailableOutputDestination(documentKey);
            }
        }
        controller.setThereHasBeenAnExplicitResultDocument();
    }
