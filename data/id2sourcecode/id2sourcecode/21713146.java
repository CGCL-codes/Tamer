    public Document load(java.net.URL url) throws DOMTestLoadException {
        try {
            java.io.InputStream stream = url.openStream();
            return (org.w3c.dom.Document) createDocument.invoke(domFactory, new Object[] { url.toString(), stream });
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            throw new DOMTestLoadException(ex.getTargetException());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DOMTestLoadException(ex);
        }
    }
