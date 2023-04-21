    public static void saveFile(InputStream from, IPath to) throws IOException {
        File toFile = to.toFile();
        if (toFile.isDirectory()) throw new IOException("File Save: destination is not a valid file name.");
        if (toFile.exists()) {
            if (!toFile.canWrite()) throw new IOException("File Save: " + "destination file is unwriteable: " + to.lastSegment());
        } else {
            String parent = toFile.getParent();
            if (parent == null) parent = System.getProperty("user.dir");
            File dir = new File(parent);
            if (!dir.exists()) throw new IOException("File Save: " + "destination directory doesn't exist: " + parent);
            if (dir.isFile()) throw new IOException("File Save: " + "destination is not a directory: " + parent);
            if (!dir.canWrite()) throw new IOException("File Save: " + "destination directory is unwriteable: " + parent);
        }
        OutputStream output = null;
        try {
            output = new BufferedOutputStream(new FileOutputStream(toFile));
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = from.read(buffer)) != -1) output.write(buffer, 0, bytesRead);
        } finally {
            if (from != null) try {
                from.close();
            } catch (IOException e) {
            }
            if (output != null) try {
                output.close();
            } catch (IOException e) {
            }
        }
    }
