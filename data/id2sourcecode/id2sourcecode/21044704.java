    public static void UnZipEntry(File dir, InputStream zinputStream, String entryName, boolean isDirectory) throws IOException {
        File f = new File(dir, entryName);
        try {
            String p = f.getParent();
            File dirParent = null;
            if (p != null) dirParent = new File(p);
            if (dirParent != null) dirParent.mkdirs();
            if (isDirectory) {
                f.mkdirs();
            } else {
                byte buffer[] = new byte[1024];
                int length = 0;
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                    while ((length = zinputStream.read(buffer)) >= 0) fos.write(buffer, 0, length);
                    fos.close();
                    fos = null;
                } finally {
                    if (fos != null) try {
                        fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        } catch (FileNotFoundException ex) {
        }
    }
