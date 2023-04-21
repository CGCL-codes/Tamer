    public static void main(String[] args) throws Exception {
        File file = new File(FILE_LIST);
        List relativePaths = FileUtils.readLines(file);
        String backupDIR = BACKUP_DSTN_DIR_TO + new SimpleDateFormat("d_M_yy__h_mm").format(new Date());
        File backupDirObj = new File(backupDIR);
        if (!backupDirObj.exists()) {
            backupDirObj.mkdirs();
            System.out.println("created backup dir : " + backupDIR);
        }
        for (Iterator iterator = relativePaths.iterator(); iterator.hasNext(); ) {
            String relFilePath = (String) iterator.next();
            String srcFile = SRC_DIR + relFilePath;
            String dstnFile = DSTN_DIR + relFilePath;
            relFilePath = relFilePath.replaceAll("\\\\", "/");
            String relDirPath = relFilePath.substring(0, relFilePath.lastIndexOf('/'));
            String backupDirIndividual = backupDIR + File.separator + relDirPath;
            File backupDirIndividualObj = new File(backupDirIndividual);
            if (backupDirIndividualObj.exists()) {
                backupDirIndividualObj.mkdirs();
            }
            System.out.println("Backing up " + dstnFile + " to " + backupDirIndividualObj.getAbsolutePath());
            File dstnFileObj = new File(dstnFile);
            if (dstnFileObj.exists()) {
                FileUtils.copyFileToDirectory(dstnFileObj, backupDirIndividualObj);
            } else {
                System.err.println("No file exists to backup ..." + dstnFileObj.getAbsolutePath());
            }
            System.out.println("Copying " + srcFile + " to " + dstnFile);
            FileUtils.copyFile(new File(srcFile), dstnFileObj);
        }
    }
