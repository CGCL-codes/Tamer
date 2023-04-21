    public ImageDiskCopyDM(String hostname, int port, String userid, String password, String target_identifier, String image_disk_number, String source_image_name, String source_image_disk_number, String image_disk_allocation_type, String allocation_area_name_or_volser, String image_disk_mode, String read_password, String write_password, String multi_password) {
        this();
        setHostname(hostname);
        setPort(port);
        setUserid(userid);
        setPassword(password);
        setTarget_identifier(target_identifier);
        set_imageDiskNumber(image_disk_number);
        set_sourceImageName(source_image_name);
        set_sourceImageDiskNumber(source_image_disk_number);
        set_imageDiskAllocationType(image_disk_allocation_type);
        set_allocationAreaNameOrVolser(allocation_area_name_or_volser);
        set_imageDiskMode(image_disk_mode);
        set_readPassword(read_password);
        set_writePassword(write_password);
        set_multiPassword(multi_password);
    }
