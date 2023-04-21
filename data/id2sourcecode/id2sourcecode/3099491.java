    public static boolean fileExists(String _codebase, String _filename) {
        if (_filename == null) {
            return false;
        }
        if (cacheImages.get(_filename) != null) {
            return true;
        }
        if (_codebase != null) {
            if (_codebase.startsWith("file:")) {
                _codebase = "file:///" + _codebase.substring(6);
            }
            if (!_codebase.endsWith("/")) {
                _codebase += "/";
            }
        }
        int index = _filename.indexOf('+');
        if (index >= 0) {
            return fileExistsInJar(_codebase, _filename.substring(0, index), _filename.substring(index + 1));
        } else if (_codebase == null) {
            java.io.File file = new java.io.File(_filename);
            return file.exists();
        } else {
            try {
                java.net.URL url = new java.net.URL(_codebase + _filename);
                java.io.InputStream stream = url.openStream();
                stream.close();
                return true;
            } catch (Exception exc) {
                return false;
            }
        }
    }
