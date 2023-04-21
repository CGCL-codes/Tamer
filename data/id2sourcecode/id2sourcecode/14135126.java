    public String execute() throws IOException {
        ActionContext ac = ActionContext.getContext();
        ServletContext sc = (ServletContext) ac.get(StrutsStatics.SERVLET_CONTEXT);
        File uploadDir = new File(sc.getRealPath("/WEB-INF/upload"));
        if (uploadDir.exists() == false) {
            uploadDir.mkdirs();
        }
        FileUtils.copyFile(upload, new File(uploadDir, uploadFileName));
        fileSize = FileUtils.byteCountToDisplaySize(upload.length());
        return "success";
    }
