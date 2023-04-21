    public synchronized boolean update() throws IOException {
        if (_resource != null && !_resource.exists()) {
            clear();
            return true;
        }
        long lm = _resource.lastModified();
        if (lm == _lastModified && (_buf != null || _list != null)) return false;
        _lastModified = lm;
        if (_resource.isDirectory()) _list = _resource.list();
        if (_list == null) {
            int l = (int) _resource.length();
            if (l < 0) l = 1024;
            ByteArrayOutputStream2 bout = new ByteArrayOutputStream2(l);
            InputStream in = _resource.getInputStream();
            try {
                IO.copy(in, bout);
            } finally {
                in.close();
            }
            _buf = bout.getBuf();
            if (_buf.length != l) _buf = bout.toByteArray();
        }
        return true;
    }
