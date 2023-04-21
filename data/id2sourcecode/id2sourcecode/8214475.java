    public byte[] getSnapshot() throws Exception {
        File _f = new File(SNAPSHOT_FILE);
        if (!_f.exists()) {
            return new byte[0];
        }
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        FileInputStream _fis = new FileInputStream(_f);
        try {
            while (_fis.available() > 0) {
                _baos.write(_fis.read());
            }
        } finally {
            _fis.close();
        }
        return _baos.toByteArray();
    }
