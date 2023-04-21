    @Actions({ @Action(value = "/uploadFacilityIF", results = { @Result(name = "success", location = "/message_upload.jsp"), @Result(name = "error", location = "/message_upload.jsp") }), @Action(value = "/uploadRoomTypeFacilityIF", results = { @Result(name = "success", location = "/message_upload.jsp"), @Result(name = "error", location = "/message_upload.jsp") }), @Action(value = "/uploadFacility", results = { @Result(type = "json", name = "success", params = { "excludeProperties", "session,upload,uploadFileName,uploadContentType,name,structureService,roomTypeService,roomService,imageService,facilityService" }), @Result(type = "json", name = "error", params = { "excludeProperties", "session,upload,uploadFileName,uploadContentType,name,structureService,roomTypeService,roomService,imageService,facilityService" }) }), @Action(value = "/uploadRoomTypeFacility", results = { @Result(type = "json", name = "success", params = { "excludeProperties", "session,upload,uploadFileName,uploadContentType,name,structureService,roomTypeService,roomService,imageService,facilityService" }), @Result(type = "json", name = "error", params = { "excludeProperties", "session,upload,uploadFileName,uploadContentType,name,structureService,roomTypeService,roomService,imageService,facilityService" }) }) })
    public String uploadRoomOrTypeFacility() throws IOException {
        ServletContext context = null;
        String imgPath = null;
        File target = null;
        if (this.getFacilityService().findUploadedFacilityByName(this.getIdStructure(), this.getName()) != null) {
            message.setResult(Message.ERROR);
            message.setDescription(getText("facilityAlreadyPresentError"));
            return ERROR;
        }
        ;
        context = ServletActionContext.getServletContext();
        imgPath = context.getRealPath("/") + "resources/" + this.getIdStructure() + "/facilities/roomOrRoomType/";
        target = new File(imgPath + this.getUploadFileName());
        FileUtils.copyFile(this.getUpload(), target);
        this.setRoomFacility(new Facility());
        this.getRoomFacility().setName(this.getName());
        this.getRoomFacility().setFileName(this.getUploadFileName());
        this.getRoomFacility().setId_structure(this.getIdStructure());
        this.getFacilityService().insertUploadedFacility(this.getRoomFacility());
        message.setResult(Message.SUCCESS);
        message.setDescription(getText("facilityAddSuccessAction"));
        return SUCCESS;
    }
