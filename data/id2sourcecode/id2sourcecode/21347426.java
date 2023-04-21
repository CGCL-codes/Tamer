    public static File createNormalFile(File parent, String fileName, boolean overwrite, boolean ignore) throws IOException {
        File file = new File(parent, fileName);
        if (file.createNewFile()) {
            System.out.println("Created file " + file);
            return file;
        }
        if (!file.exists() || file.isDirectory()) {
            throw new IOException(file.getPath() + " : could not create normal file.");
        }
        if (ignore) {
            System.out.println(file + " already exists; skipping");
            return null;
        }
        if (!overwrite) {
            throw new IOException(file.getPath() + " : already exists; please remove it or use the -overwrite or -ignore option.");
        }
        System.out.println("Overwriting existing file " + file);
        return file;
    }
