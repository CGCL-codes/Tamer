    public void setContext(ContentPanelContext newContext) throws UnsupportedContextException {
        try {
            URLConnection urlc = newContext.getURL().openConnection();
            String type = urlc.getContentType();
            if (type == null || !type.startsWith("model")) {
                throw new UnsupportedContextException();
            }
            if (type.equals("model/vrml")) {
                loaderThread = new Web3DLoaderThread(newContext);
                if (getHost() != null) {
                    getHost().contentPanelStartedLoading(this);
                }
                loaderThread.start();
            } else {
                throw new UnsupportedContextException();
            }
        } catch (IOException ioe) {
            throw new UnsupportedContextException();
        }
        super.setContext(newContext);
    }
