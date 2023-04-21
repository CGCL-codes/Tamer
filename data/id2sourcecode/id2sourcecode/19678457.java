    @Actions({ @Action(value = "/uploadStructureImageIF", results = { @Result(name = "success", location = "/message_upload.jsp"), @Result(name = "error", location = "/message_upload.jsp") }), @Action(value = "/uploadStructureImage", results = { @Result(type = "json", name = "success", params = { "excludeProperties", "session,upload,uploadFileName,uploadContentType,name,structureService,roomTypeService,roomService,imageService,facilityService" }), @Result(type = "json", name = "error", params = { "excludeProperties", "session,upload,uploadFileName,uploadContentType,name,structureService,roomTypeService,roomService,imageService,facilityService" }) }) })
    public String uploadStructureImage() throws IOException {
        ServletContext context = null;
        String imgPath = null;
        File target = null;
        if (this.getImageService().findStructureImageByName(this.getIdStructure(), this.getName()) != null) {
            message.setResult(Message.ERROR);
            message.setDescription(getText("structureImageAlreadyPresentError"));
            return ERROR;
        }
        context = ServletActionContext.getServletContext();
        imgPath = context.getRealPath("/") + "resources/" + this.getIdStructure() + "/images/structure/";
        target = new File(imgPath + this.getUploadFileName());
        FileUtils.copyFile(this.getUpload(), target);
        this.setImage(new Image());
        this.getImage().setName(this.getName());
        this.getImage().setFileName(this.getUploadFileName());
        this.getImage().setId_structure(this.getIdStructure());
        this.getImageService().insertStructureImage(this.getImage());
        message.setResult(Message.SUCCESS);
        message.setDescription(getText("structureImageAddSuccessAction"));
        return SUCCESS;
    }
