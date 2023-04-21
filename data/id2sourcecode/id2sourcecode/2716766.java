    public void moveUploadFile(Forum forum, long tobid) throws IOException {
        List fileList = forum.getAttachFileName();
        for (int i = 0; i < fileList.size(); i++) {
            String filename = (String) fileList.get(i);
            File fromFile = new File(BBSCSUtil.getUpFilePath(forum.getBoardID(), forum.getPostTime()) + filename);
            File toFile = new File(BBSCSUtil.getUpFilePath(tobid, forum.getPostTime()) + filename);
            FileUtils.copyFile(fromFile, toFile);
            FileUtils.forceDelete(fromFile);
            File fromFileSmall = new File(BBSCSUtil.getUpFilePath(forum.getBoardID(), forum.getPostTime()) + filename + Constant.IMG_SMALL_FILEPREFIX);
            if (fromFileSmall.exists()) {
                File toFileSmall = new File(BBSCSUtil.getUpFilePath(tobid, forum.getPostTime()) + filename + Constant.IMG_SMALL_FILEPREFIX);
                FileUtils.copyFile(fromFileSmall, toFileSmall);
                FileUtils.forceDelete(fromFileSmall);
            }
        }
    }
