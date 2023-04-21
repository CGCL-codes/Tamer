    private InputStream getInput() {
        if (!_configFile.canRead()) {
            return null;
        }
        try {
            final InputStream in = new FileInputStream(_configFile);
            final MessageDigest inputDigest = MessageDigest.getInstance("MD5");
            return new DigestInputStream(in, inputDigest) {

                boolean isClosed = false;

                @Override
                public void close() throws IOException {
                    if (!isClosed) {
                        super.close();
                        _inputDigest = inputDigest.digest();
                    }
                    isClosed = true;
                }
            };
        } catch (final Exception e) {
            return null;
        }
    }
