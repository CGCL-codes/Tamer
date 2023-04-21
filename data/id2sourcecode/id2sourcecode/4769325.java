    void loadDefaults(String _file) throws FasperException {
        if (!_file.equals("")) {
            Properties _props = Utils.loadProps(new File(_file));
            setDefaults(_props);
        } else {
            try {
                URL url = Defs.class.getResource("resources/init.init");
                Properties _props = new Properties();
                _props.load(url.openStream());
                setDefaults(_props);
            } catch (Exception e) {
                throw new FasperException("DEFAULTS_LOAD_FAILED", "Can not load defaults in 'resources/init.init'");
            }
        }
    }
