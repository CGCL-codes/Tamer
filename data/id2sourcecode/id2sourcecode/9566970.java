    private void init(File archive) {
        try {
            resources = new HashMap();
            FileInputStream archiveFile = new FileInputStream(archive);
            ZipInputStream zip = new ZipInputStream(new CipherInputStream(archiveFile, SecureUtils.getDecryptingCipher(password, true)));
            ZipEntry ze;
            while ((ze = zip.getNextEntry()) != null) {
                byte[] buff = new byte[1024];
                int l;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((l = zip.read(buff)) != -1) bos.write(buff, 0, l);
                bos.close();
                resources.put(ze.getName(), new Value(new ByteArrayInputStream(bos.toByteArray()), bos.size()));
            }
            zip.close();
        } catch (Exception e) {
            throw new OXFException(e);
        }
    }
