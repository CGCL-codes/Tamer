    public static void fixCrLf(File f) {
        if (f.isFile()) {
            FileCopier.writeStringToFile(f, LinePositionUtil.useUnixNewlines(FileCopier.readFileToString(f)));
        } else {
            File[] children = f.listFiles();
            for (int i = 0; i < children.length; i++) {
                fixCrLf(children[i]);
            }
        }
    }
