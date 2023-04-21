    private static void addFileRecursively(DiffStatus ds, File file) {
        Element e1 = ds.new Element(FileUtil.relativeWorkcopyPath(file), Actions.UNVERSIONED);
        ds.addElement(e1);
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                addFileRecursively(ds, subFile);
            }
        }
    }
