    public static boolean storeFile(Databit dbit, boolean overwrite, FileItem fi, String uname) throws Exception {
        if (fi != null && !fi.getName().trim().equals("") && fi.getSize() > 0) {
            String fileDir = new SWAMPAPI().doGetProperty("ATTACHMENT_DIR", uname);
            if (!(new File(fileDir)).canWrite()) {
                throw new Exception("Cannot write to configured path: " + fileDir);
            }
            String fileName = fi.getName();
            if (fileName.indexOf("/") >= 0) fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            if (fileName.indexOf("\\") >= 0) fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
            File file = new File(fileDir + fs + dbit.getId() + "-" + fileName);
            if (!overwrite) {
                if (!file.createNewFile()) {
                    throw new Exception("Cannot write to file: " + file.getName() + ". File already exists?");
                }
            } else {
                if (file.exists()) {
                    file.delete();
                }
                File oldFile = new File(fileDir + fs + dbit.getId() + "-" + dbit.getValue());
                if (oldFile.exists()) {
                    Logger.DEBUG("Deleting old file: " + oldFile.getPath());
                    oldFile.delete();
                }
            }
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(fi.get());
            stream.close();
            return true;
        } else {
            return false;
        }
    }
