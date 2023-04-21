    public static void backupAccessFile() throws IOException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date myDate = new Date();
        String dateext = "_" + df.format(myDate);
        String path = GlobalProperties.getFILEPATH();
        String ext = path.contains(".") ? path.substring(path.lastIndexOf("."), path.length()) : "";
        String split = path.contains("\\") ? "\\" : "/";
        String bck = GlobalProperties.getBackupPath() + path.substring(path.lastIndexOf(split), path.contains(".") ? path.indexOf(".") : path.length());
        FileUtils.copyFile(new File(path), new File(bck + dateext + ext));
    }
