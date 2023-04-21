    private static String[] scanDir(File srcDir, String srcDirExt) {
        Vector filenames = new Vector();
        File[] files = (new File(srcDir, srcDirExt)).listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile() && (files[i].getName().endsWith(".java"))) {
                filenames.add(srcDir + srcDirExt + File.separator + files[i].getName());
            } else if (files[i].isDirectory()) {
                filenames.addAll(Arrays.asList(scanDir(srcDir, srcDirExt + File.separator + files[i].getName())));
            }
        }
        String[] ret = new String[filenames.size()];
        filenames.copyInto(ret);
        return (ret);
    }
