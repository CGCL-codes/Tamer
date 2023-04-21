    public static String getImagePath(MP_POST mpPost) throws IOException, FileNotFoundException {
        String ext = ".jpg";
        if (mpPost.getImageMimeType().equals("image/gif")) {
            ext = ".gif";
        }
        Preferences mailProps = PrefsMgr.getInstance().getPreferences();
        String imgDir = mailProps.get(WMLBlogger.WMLBLOG_IMAGE_PATH, null);
        String iDir = imgDir + "/" + mpPost.getUserId().replace(' ', '_') + "/" + mpPost.getChannelName().replace(' ', '_');
        File iDirFile = new File(iDir);
        iDirFile.mkdirs();
        return iDir + "/p" + file_dateFormat.format(mpPost.getPostDate()) + ext;
    }
