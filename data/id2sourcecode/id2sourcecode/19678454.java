    @Actions({ @Action(value = "/uploadStructureFacilityIF", results = { @Result(name = "success", location = "/message_upload.jsp"), @Result(name = "error", location = "/message_upload.jsp") }), @Action(value = "/uploadStructureFacility", results = { @Result(type = "json", name = "success", params = { "excludeProperties", "session,upload,uploadFileName,uploadContentType,name,structureService,roomTypeService,roomService,imageService,facilityService" }), @Result(type = "json", name = "error", params = { "excludeProperties", "session,upload,uploadFileName,uploadContentType,name,structureService,roomTypeService,roomService,imageService,facilityService" }) }) })
    public String uploadStructureFacility() throws IOException {
        ServletContext context = null;
        String imgPath = null;
        File target = null;
        if (this.getFacilityService().findStructureFacilityByName(this.getIdStructure(), this.getName()) != null) {
            message.setResult(Message.ERROR);
            message.setDescription(getText("facilityAlreadyPresentError"));
            return ERROR;
        }
        context = ServletActionContext.getServletContext();
        imgPath = context.getRealPath("/") + "resources/" + this.getIdStructure() + "/facilities/structure/";
        target = new File(imgPath + this.getUploadFileName());
        FileUtils.copyFile(this.upload, target);
        this.setStructureFacility(new Facility());
        this.getStructureFacility().setName(this.getName());
        this.getStructureFacility().setFileName(this.getUploadFileName());
        this.getStructureFacility().setId_structure(this.getIdStructure());
        this.getFacilityService().insertStructureFacility(this.getStructureFacility());
        message.setResult(Message.SUCCESS);
        message.setDescription(getText("facilityAddSuccessAction"));
        return SUCCESS;
    }
