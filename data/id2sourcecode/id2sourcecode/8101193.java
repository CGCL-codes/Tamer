        private ZipInputStream openImpl() throws IOException {
            if (cachedBits == null) try {
                URLConnection uc = url.openConnection();
                lastModified = uc.getLastModified();
                cachedBits = new ByteArray(uc.getInputStream(), true).getBytes();
            } catch (InterruptedException e) {
                throw new IOException("interrupted while opening " + getName());
            }
            return new ZipInputStream(new ByteArrayInputStream(cachedBits));
        }
