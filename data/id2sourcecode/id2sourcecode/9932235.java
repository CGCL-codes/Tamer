    private byte[] getEncryptionkey_convergent() throws IOException {
        if (_encryptKey == null) {
            int BLOCK_SIZE = 64 * 1024;
            SHA256d _hasher = Hasher.getConvergencehasher(_params, _convergence);
            _filehandle.seek(0);
            byte[] data = new byte[BLOCK_SIZE];
            while (true) {
                int status = _filehandle.read(data);
                if (status == -1) break;
                byte[] tmp = Arrays.copyOf(data, status);
                _hasher.update(tmp);
            }
            _filehandle.seek(0);
            _encryptKey = _hasher.digest();
        }
        return _encryptKey;
    }
