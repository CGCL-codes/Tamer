    public static String saveRequestAvatarFile(File file, String filename, String fileContentType, int userid, int maxAllowFileSize) throws IOException {
        if (Utils.isImgFilename(filename)) {
            StringBuilder savedir = new StringBuilder();
            savedir.append("avatars/upload/");
            int t1 = (int) ((double) userid / (double) 10000);
            savedir.append(t1);
            savedir.append("/");
            int t2 = (int) ((double) userid / (double) 200);
            savedir.append(t2);
            savedir.append("/");
            String newfilename = savedir.toString() + userid + filename.substring(filename.lastIndexOf(".")).toLowerCase();
            if (file.length() <= maxAllowFileSize) {
                FileUtils.copyFile(file, new File(ConfigLoader.getConfig().getWebpath() + newfilename));
                if (logger.isDebugEnabled()) {
                    logger.debug("保存头像文件：{} 成功", newfilename);
                }
                return newfilename;
            }
        }
        return "";
    }
