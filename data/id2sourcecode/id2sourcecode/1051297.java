    public static boolean enhanceZipFile(ClassFileEnhancer enhancer, ZipInputStream zip_in, ZipOutputStream zip_out) throws EnhancerUserException, EnhancerFatalError {
        boolean enhanced = false;
        try {
            CRC32 crc32 = new CRC32();
            ZipEntry entry;
            while ((entry = zip_in.getNextEntry()) != null) {
                InputStream in = zip_in;
                final ZipEntry out_entry = new ZipEntry(entry);
                if (isClassFileEntry(entry)) {
                    in = openZipEntry(zip_in);
                    in.mark(Integer.MAX_VALUE);
                    final ByteArrayOutputStream tmp = new ByteArrayOutputStream();
                    if (enhancer.enhanceClassFile(in, tmp)) {
                        enhanced = true;
                        final byte[] bytes = tmp.toByteArray();
                        tmp.close();
                        in.close();
                        modifyZipEntry(out_entry, bytes, crc32);
                        in = new ByteArrayInputStream(bytes);
                    } else {
                        in.reset();
                    }
                }
                zip_out.putNextEntry(out_entry);
                copyZipEntry(in, zip_out);
                zip_out.closeEntry();
                if (in != zip_in) {
                    in.close();
                }
            }
        } catch (IOException ex) {
            throw new EnhancerFatalError(ex);
        }
        return enhanced;
    }
