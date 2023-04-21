    public static void zipFilesTo(Vector<File> fileVector, String baseDir, File destFile) {
        FileOutputStream ops = null;
        ZipOutputStream zos = null;
        int basedirlen = baseDir.length();
        if (!baseDir.endsWith(File.separator)) basedirlen++;
        try {
            ops = new FileOutputStream(destFile);
            zos = new ZipOutputStream(ops);
            Iterator<File> iter = fileVector.iterator();
            while (iter.hasNext()) {
                File file = iter.next();
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    String name = file.getPath().substring(basedirlen);
                    name = name.replace('\\', '/');
                    ZipEntry zi = new ZipEntry(name);
                    zos.putNextEntry(zi);
                    copystream(fis, zos);
                    zos.closeEntry();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fis != null) fis.close();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zos != null) zos.close(); else if (ops != null) ops.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
