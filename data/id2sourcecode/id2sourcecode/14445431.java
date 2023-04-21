    public static int copy(Integer fileid) throws Exception {
        String prefix = getDirectory(fileid);
        String filename = Configuration.diskStorage + prefix + "/" + fileid;
        int newid = UniqueID.getUniqueId(1);
        if (newid == -1) {
            String message = "DiskStorage.copy(): couldn't get an unique id for the file.";
            Configuration.logger.error(message);
            throw new Exception(message);
        }
        String newprefix = getDirectory(newid);
        File dir = new File(Configuration.diskStorage + newprefix);
        if (!dir.exists()) dir.mkdirs();
        String newfilename = Configuration.diskStorage + newprefix + "/" + newid;
        FileUtils.copyFile(filename, newfilename);
        return newid;
    }
