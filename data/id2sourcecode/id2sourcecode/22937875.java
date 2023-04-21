    public void setContext(ContentPanelContext newContext) throws UnsupportedContextException {
        try {
            URLConnection urlc = newContext.getURL().openConnection();
            String type = urlc.getContentType();
            if (type == null || !type.startsWith("image")) {
                throw new UnsupportedContextException();
            }
        } catch (IOException ioe) {
            throw new UnsupportedContextException();
        }
        image = getToolkit().createImage(newContext.getURL());
        loading = true;
        if (getHost() != null) {
            getHost().contentPanelStartedLoading(this);
        }
        getToolkit().prepareImage(image, -1, -1, this);
        imagePanel.repaint();
        scrollPane.doLayout();
        super.setContext(newContext);
    }
