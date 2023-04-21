    public static void unZip(String zipFile, String ToPath) {
        try {
            ZipFile zipfile = new ZipFile(zipFile);
            Enumeration<?> zipenum = zipfile.getEntries();
            while (zipenum.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) zipenum.nextElement();
                File newFile = new File(ToPath, ze.getName());
                ReadableByteChannel rc = Channels.newChannel(zipfile.getInputStream(ze));
                if (ze.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    FileOutputStream fos = new FileOutputStream(newFile);
                    FileChannel fc = fos.getChannel();
                    fc.transferFrom(rc, 0, ze.getSize());
                    fos.close();
                }
            }
            zipfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
