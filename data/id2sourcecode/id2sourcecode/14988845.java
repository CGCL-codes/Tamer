    private void archiveAndEncrypt(File root, ZipOutputStream zip) throws Exception {
        if (root.isDirectory()) {
            if (root.getName().equals("CVS")) return;
            File[] files = root.listFiles();
            for (int i = 0; i < files.length; i++) {
                archiveAndEncrypt(files[i], zip);
            }
        } else {
            String f = computePath(root.getPath());
            System.out.println("Adding " + f);
            ZipEntry ze = new ZipEntry(f);
            zip.putNextEntry(ze);
            copyFile(root, zip);
            zip.closeEntry();
        }
    }
