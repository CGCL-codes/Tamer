    @Actions({ @Action(value = "/uploadRoomImageIF", results = { @Result(name = "success", location = "/message_upload.jsp"), @Result(name = "error", location = "/message_upload.jsp") }), @Action(value = "/uploadRoomImage", results = { @Result(type = "json", name = "success", params = { "excludeProperties", "session,upload,uploadFileName,uploadContentType,name,structureService,roomTypeService,roomService,imageService,facilityService" }), @Result(type = "json", name = "error", params = { "excludeProperties", "session,upload,uploadFileName,uploadContentType,name,structureService,roomTypeService,roomService,imageService,facilityService" }) }) })
    public String uploadRoomImage() throws IOException {
        Room aRoom = null;
        ServletContext context = null;
        String imgPath = null;
        File target = null;
        if (this.getImageService().findRoomImageByName(this.getIdStructure(), this.getName()) != null) {
            message.setResult(Message.ERROR);
            message.setDescription(getText("roomImageAlreadyPresentError"));
            return ERROR;
        }
        ;
        aRoom = this.getRoomService().findRoomById(this.getRoom().getId());
        if (aRoom == null) {
            message.setResult(Message.ERROR);
            message.setDescription(getText("roomImageNoRoomError"));
            return ERROR;
        }
        context = ServletActionContext.getServletContext();
        imgPath = context.getRealPath("/") + "resources/" + this.getIdStructure() + "/images/room/";
        target = new File(imgPath + this.getUploadFileName());
        FileUtils.copyFile(this.getUpload(), target);
        this.setImage(new Image());
        this.getImage().setName(this.getName());
        this.getImage().setFileName(this.getUploadFileName());
        this.getImage().setId_room(this.getRoom().getId());
        this.getImageService().insertRoomImage(this.getImage());
        message.setResult(Message.SUCCESS);
        message.setDescription(getText("roomImageAddSuccessAction"));
        return SUCCESS;
    }
