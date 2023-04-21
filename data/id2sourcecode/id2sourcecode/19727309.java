     *   @param file extension eg csv or html
     */
    static int file_counter = 0;

    public static java.io.File createTempFile(String start, String extension) throws java.io.IOException {
        file_counter++;
        java.io.File directory = null;
        String[] dirs = { "c:" + java.io.File.separator + "temp", "c:" + java.io.File.separator + "tmp", java.io.File.separator + "macintosh hd", java.io.File.separator + "tmp", java.io.File.separator + "usr" + java.io.File.pathSeparator + "tmp", "noluck" };
        for (int i = 0; i < dirs.length; i++) {
            directory = new java.io.File(dirs[i]);
            if (directory.isDirectory() && directory.canWrite() && directory.canRead()) {
                break;
            }
            if (i == (dirs.length - 1)) {
                System.err.println("Desparate in TmpFile therefore trying to create " + dirs[0]);
                directory = new java.io.File(dirs[0]);
                directory.mkdirs();
            }
        }
        java.io.File file = null;
        String file_name = "";
        while (true) {
            file_counter++;
            file_name = directory.getAbsolutePath() + java.io.File.separator + start + "-" + file_counter + "." + extension;
            file = new java.io.File(file_name);
            System.err.println("file_name " + file_name + " can write " + file.canWrite() + " can read " + file.canRead());
            if ((file.canWrite() && file.canRead()) || !file.exists()) {
                break;
            }
        }
        if (file == null) {
