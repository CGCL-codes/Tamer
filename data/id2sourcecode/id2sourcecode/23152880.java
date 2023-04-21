    public ClassFileInfo read(JavaFileObject fo) throws IOException, ConstantPoolException {
        InputStream in = fo.openInputStream();
        try {
            SizeInputStream sizeIn = null;
            MessageDigest md = null;
            if (options.sysInfo || options.verbose) {
                try {
                    md = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException ignore) {
                }
                in = new DigestInputStream(in, md);
                in = sizeIn = new SizeInputStream(in);
            }
            ClassFile cf = ClassFile.read(in, attributeFactory);
            byte[] digest = (md == null) ? null : md.digest();
            int size = (sizeIn == null) ? -1 : sizeIn.size();
            return new ClassFileInfo(fo, cf, digest, size);
        } finally {
            in.close();
        }
    }
