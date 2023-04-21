    public static String uploadFile(String fileName, File upload) throws IOException {
        String fullFileName = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath(UPLOAD_FOLDER);
        File theFile = new File(fullFileName + "\\" + fileName);
        FileUtils.copyFile(upload, theFile);
        return UPLOAD_FOLDER + "/" + fileName;
    }
