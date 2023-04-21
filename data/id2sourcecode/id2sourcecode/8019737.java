    public void transform(InputStream is, OutputStream os) throws IOException {
        if (!is.markSupported()) {
            is = new BufferedInputStream(is, LOCHDR);
        }
        is.mark(LOCHDR);
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry e = zis.getNextEntry();
        if (e == null) {
            is.reset();
            this.delegate2.transform(is, os);
            return;
        }
        ZipOutputStream zos = new ZipOutputStream(os);
        try {
            do {
                {
                    ZipEntry e2 = new ZipEntry(e.getName());
                    e2.setComment(e.getComment());
                    e2.setExtra(e.getExtra());
                    e2.setTime(e.getTime());
                    zos.putNextEntry(e2);
                }
                Glob subselector;
                {
                    final String prefix = e.getName() + '!';
                    subselector = new Glob() {

                        @Override
                        public boolean matches(String subject) {
                            return ZipContentsTransformer.this.selector.matches(prefix + subject);
                        }
                    };
                }
                new ZipContentsTransformer(subselector, this.delegate1, this.delegate1 == this.delegate2 || this.selector.matches(e.getName()) ? this.delegate1 : this.delegate2).transform(zis, zos);
                e = zis.getNextEntry();
            } while (e != null);
        } catch (IOException ioe) {
            IOException ioe2 = new IOException("Transforming ZIP entry '" + e.getName() + "': " + ioe.getMessage());
            ioe2.initCause(ioe);
            throw ioe2;
        } catch (RuntimeException re) {
            throw new RuntimeException("Transforming ZIP entry '" + e.getName() + "': " + re.getMessage(), re);
        }
        zos.finish();
    }
