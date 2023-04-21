    public String importExcel() {
        String targetDirectory = getServletRequest().getRealPath("/upload");
        String targetFileName;
        try {
            targetFileName = uploadFileName;
            File target = new File(targetDirectory, targetFileName);
            if (target.exists()) {
                System.out.println("exists");
            } else {
                System.out.println("no exists");
            }
            FileUtils.copyFile(upload, target);
            if (target.exists()) {
                System.out.println("exists");
            } else {
                System.out.println("no exists");
            }
            upload.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "importResult";
    }
