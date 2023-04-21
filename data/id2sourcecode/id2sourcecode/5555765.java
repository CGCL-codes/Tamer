    public int readinto(PyObject buf) {
        if (!(buf instanceof PyArray)) {
            if (buf instanceof PyString) {
                throw Py.TypeError("Cannot use string as modifiable buffer");
            }
            throw Py.TypeError("argument 1 must be read-write buffer, not " + buf.getType().fastGetName());
        }
        PyArray array = (PyArray) buf;
        String read = read(array.__len__());
        for (int i = 0; i < read.length(); i++) {
            array.set(i, new PyString(read.charAt(i)));
        }
        return read.length();
    }
