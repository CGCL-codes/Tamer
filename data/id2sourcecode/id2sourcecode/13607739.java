    public final void save(OutputStream outputStream, Map<?, ?> options) throws IOException {
        if (errors != null) {
            errors.clear();
        }
        if (warnings != null) {
            warnings.clear();
        }
        options = mergeMaps(options, defaultSaveOptions);
        ZipOutputStream zipOutputStream = null;
        if (useZip() || (options != null && Boolean.TRUE.equals(options.get(Resource.OPTION_ZIP)))) {
            zipOutputStream = new ZipOutputStream(outputStream) {

                @Override
                public void finish() throws IOException {
                    super.finish();
                    def.end();
                }

                @Override
                public void flush() {
                }

                @Override
                public void close() throws IOException {
                    try {
                        super.flush();
                    } catch (IOException exception) {
                    }
                    super.close();
                }
            };
            zipOutputStream.putNextEntry(newContentZipEntry());
            outputStream = zipOutputStream;
        }
        URIConverter.Cipher cipher = options != null ? (URIConverter.Cipher) options.get(Resource.OPTION_CIPHER) : null;
        if (cipher != null) {
            try {
                outputStream = cipher.encrypt(outputStream);
            } catch (Exception e) {
                throw new IOWrappedException(e);
            }
        }
        doSave(outputStream, options);
        if (cipher != null) {
            try {
                cipher.finish(outputStream);
            } catch (Exception e) {
                throw new IOWrappedException(e);
            }
        }
        setModified(false);
        if (zipOutputStream != null) {
            zipOutputStream.finish();
        }
    }
