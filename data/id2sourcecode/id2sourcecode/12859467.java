                @Override
                public void close() throws IOException {
                    if (!isClosed) {
                        super.close();
                        _inputDigest = inputDigest.digest();
                    }
                    isClosed = true;
                }
