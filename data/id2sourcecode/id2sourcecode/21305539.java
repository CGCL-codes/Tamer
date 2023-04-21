            @Override
            public void close() throws IOException {
                try {
                    byte[] digest = getMessageDigest().digest();
                    SignedZipFileOutput.this.entries.add(new Entry(path, digest));
                } finally {
                    super.close();
                }
            }
