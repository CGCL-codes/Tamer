    private static void zip(File root, File file, ZipOutputStream zos) throws IOException {
        if (file.isDirectory()) {
            String name = file.getName();
            if (name.endsWith("_zip") || name.endsWith("_jar")) {
                String rootString = root.toString();
                String fileString = file.toString();
                String zipEntryName = fileString.substring(rootString.length() + 1);
                int underscoreIndex = zipEntryName.lastIndexOf("_");
                zipEntryName = zipEntryName.substring(0, underscoreIndex) + "." + zipEntryName.substring(underscoreIndex + 1);
                ZipEntry zipEntry = new ZipEntry(changeSeparator(zipEntryName, File.separatorChar, '/'));
                zos.putNextEntry(zipEntry);
                ZipOutputStream zos2 = new ZipOutputStream(zos);
                String[] list = file.list();
                for (int i = 0; i < list.length; ++i) {
                    File item = new File(file, list[i]);
                    zip(file, item, zos2);
                }
                zos2.finish();
                zos.closeEntry();
            } else {
                String[] list = file.list();
                for (int i = 0; i < list.length; ++i) {
                    File item = new File(file, list[i]);
                    zip(root, item, zos);
                }
            }
        } else {
            String rootString = root.toString();
            String fileString = file.toString();
            String zipEntryName = fileString.substring(rootString.length() + 1);
            ZipEntry zipEntry = new ZipEntry(changeSeparator(zipEntryName, File.separatorChar, '/'));
            zos.putNextEntry(zipEntry);
            FileInputStream fos = null;
            try {
                fos = new FileInputStream(file);
                transferData(fos, zos);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
                }
            }
            zos.closeEntry();
        }
    }
