    public InputStream getStreamData() {
        try {
            if (_url != null) {
                InputStream is = _url.openStream();
                return is != null ? new BufferedInputStream(is) : null;
            }
            if (_file != null) return new BufferedInputStream(new FileInputStream(_file));
        } catch (java.io.IOException ex) {
            throw new SystemException("Unable to read " + (_url != null ? _url.toString() : _file.toString()), ex);
        }
        if (_isdata != null) return _isdata;
        return new ByteArrayInputStream(_data);
    }
