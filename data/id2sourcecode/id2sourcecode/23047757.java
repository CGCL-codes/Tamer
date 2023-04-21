    public String upload() {
        ServletContext context = ServletActionContext.getServletContext();
        String baseDir = context.getRealPath("/");
        String destFile = "/tech/java/code/ethantest/jumblebee/WebContent/uploads/buzz/" + this.FiledataFileName;
        String destFile2 = baseDir + "/uploads/buzz/" + this.FiledataFileName;
        try {
            FileUtils.copyFile(this.Filedata, new File(destFile));
            FileUtils.copyFile(this.Filedata, new File(destFile2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.FiledataFileName);
        return "upload";
    }
