    public static void main(String[] args) throws IOException, IllegalArgumentException, IllegalAccessException {
        File srcDir = new File(SRC_DIR);
        String[] list = srcDir.list();
        int counter = 1;
        System.out.println("Starting..........");
        printMemberVariables();
        for (int i = 0; i < list.length; i++) {
            String fileOrDir = list[i];
            File fileOrDirSrcObj = new File(SRC_DIR, fileOrDir + RELATIVE_FILE_PATH);
            File fileOrDirDstnObj = new File(DSTN_DIR, fileOrDir + RELATIVE_FILE_PATH);
            if (fileOrDirSrcObj.exists()) {
                System.out.println((counter++) + fileOrDirDstnObj.getAbsolutePath());
                FileUtils.copyFile(fileOrDirSrcObj, fileOrDirDstnObj);
            }
        }
        System.out.println("Done..........");
    }
