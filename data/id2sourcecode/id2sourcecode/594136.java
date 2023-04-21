    private static void fileData(File f) {
        System.out.println("Absolute path: " + f.getAbsolutePath() + "\n Can read: " + f.canRead() + "\n Can write: " + f.canWrite() + "\n getName: " + f.getName() + "\n getParent: " + f.getParent() + "\n getPath: " + f.getPath() + "\n length: " + f.length() + "\n lastModified: " + f.lastModified());
        if (f.isFile()) System.out.println("it's a file"); else if (f.isDirectory()) System.out.println("it's a directory");
    }
