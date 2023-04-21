    @Actions({ @Action(value = "/uploadRoomTypeImageIF", results = { @Result(name = "success", location = "/message_upload.jsp"), @Result(name = "error", location = "/message_upload.jsp") }), @Action(value = "/uploadRoomTypeImage", results = { @Result(type = "json", name = "success", params = { "excludeProperties", "session,upload,uploadFileName,uploadContentType,name,structureService,roomTypeService,roomService,imageService,facilityService" }), @Result(type = "json", name = "error", params = { "excludeProperties", "session,upload,uploadFileName,uploadContentType,name,structureService,roomTypeService,roomService,imageService,facilityService" }) }) })
    public String uploadRoomTypeImage() throws IOException {
        RoomType aRoomType = null;
        ServletContext context = null;
        String imgPath = null;
        File target = null;
        if (this.getImageService().findRoomTypeImageByName(this.getIdStructure(), this.getName()) != null) {
            message.setResult(Message.ERROR);
            message.setDescription(getText("roomTypeImageAlreadyPresentError"));
            return ERROR;
        }
        aRoomType = this.getRoomTypeService().findRoomTypeById(this.getRoomType().getId());
        if (aRoomType == null) {
            message.setResult(Message.ERROR);
            message.setDescription(getText("roomTypeImageNoRoomTypeError"));
            return ERROR;
        }
        context = ServletActionContext.getServletContext();
        imgPath = context.getRealPath("/") + "resources/" + this.getIdStructure() + "/images/roomType/";
        target = new File(imgPath + this.getUploadFileName());
        FileUtils.copyFile(this.getUpload(), target);
        this.setImage(new Image());
        this.getImage().setName(this.getName());
        this.getImage().setFileName(this.getUploadFileName());
        this.getImage().setId_roomType(this.getRoomType().getId());
        this.getImageService().insertRoomTypeImage(this.getImage());
        message.setResult(Message.SUCCESS);
        message.setDescription(getText("facilityImageAddSuccessAction"));
        return SUCCESS;
    }
