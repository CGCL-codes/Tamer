    public static byte[] generateDigest(String filename) throws GIEWSException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new GIEWSException("file not found");
        }
        byte[] toChapter1Digest = null;
        try {
            FileInputStream fileStream = new FileInputStream(file);
            ZipInputStream zin = new ZipInputStream(fileStream);
            MessageDigest md = MessageDigest.getInstance("SHA");
            int byteRead = 1;
            byte buffer[] = new byte[1000];
            ZipEntry ze = zin.getNextEntry();
            md = MessageDigest.getInstance("SHA");
            while (ze != null) {
                byteRead = 1;
                if (ze.getName() == "Projects.xml") {
                    while (byteRead > 0) {
                        byteRead = zin.read(buffer);
                        md.update(buffer);
                    }
                }
                zin.closeEntry();
                ze = zin.getNextEntry();
            }
            toChapter1Digest = md.digest();
            fileStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GIEWSException(e.getMessage());
        }
        return toChapter1Digest;
    }
