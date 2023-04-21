    public static File copy(InputStream from, String toFileName) throws IOException {
        File toFile = new File(toFileName);
        System.out.println("AbsolutePath toFile: " + toFile.getAbsolutePath());
        if (toFile.isDirectory()) throw new IOException("Target file " + toFileName + " is a directory when a file was expected");
        if (toFile.exists()) {
            if (!toFile.canWrite()) throw new IOException("FileCopy: " + "destination file is unwriteable: " + toFileName);
        } else {
            String parent = toFile.getParent();
            if (parent == null) parent = System.getProperty("user.dir");
            File dir = new File(parent);
            if (!dir.exists()) throw new IOException("FileCopy: " + "destination directory doesn't exist: " + parent);
            if (dir.isFile()) throw new IOException("FileCopy: " + "destination is not a directory: " + parent);
            if (!dir.canWrite()) throw new IOException("FileCopy: " + "destination directory is unwriteable: " + parent);
        }
        FileOutputStream to = null;
        try {
            to = new FileOutputStream(toFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = from.read(buffer)) != -1) to.write(buffer, 0, bytesRead);
        } finally {
            if (from != null) try {
                from.close();
            } catch (IOException e) {
                ;
            }
            if (to != null) try {
                to.close();
            } catch (IOException e) {
                ;
            }
        }
        return toFile;
    }
