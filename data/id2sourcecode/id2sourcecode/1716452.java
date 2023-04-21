    public boolean readState(String _filename, java.net.URL _codebase) {
        if (model == null) {
            return false;
        }
        try {
            java.io.InputStream in;
            if (_filename.startsWith("ejs:")) {
                in = new java.io.ByteArrayInputStream((byte[]) memory.get(_filename));
            } else if (_filename.startsWith("url:")) {
                String url = _filename.substring(4);
                if (_codebase == null || url.startsWith("http:")) {
                    ;
                } else {
                    url = _codebase + url;
                }
                in = (new java.net.URL(url)).openStream();
            } else {
                in = new java.io.FileInputStream(_filename);
            }
            java.io.BufferedInputStream bin = new java.io.BufferedInputStream(in);
            java.io.ObjectInputStream din = new java.io.ObjectInputStream(bin);
            java.lang.reflect.Field[] fields = model.getClass().getFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].get(model) instanceof java.io.Serializable) {
                    fields[i].set(model, din.readObject());
                }
            }
            din.close();
            if (view != null) {
                view.initialize();
            }
            update();
            return true;
        } catch (java.lang.Exception ioe) {
            errorMessage("Error when trying to read " + _filename);
            ioe.printStackTrace(System.err);
            return false;
        }
    }
