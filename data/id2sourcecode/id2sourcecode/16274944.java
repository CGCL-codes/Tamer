    public Response execute() throws org.json.JSONException {
        com.softwoehr.pigiron.functions.ImageDiskCopyDM pigfunc = new com.softwoehr.pigiron.functions.ImageDiskCopyDM(getHostSpecifier(), host.getPortNumber(), user.getUid(), user.getPassword(), getTargetIdentifier(), getInputArgumentString("image_disk_number"), getInputArgumentString("source_image_name"), getInputArgumentString("source_image_disk_number"), getInputArgumentString("image_disk_allocation_type"), getInputArgumentString("allocation_area_name_or_volser"), getInputArgumentString("image_disk_mode"), getInputArgumentString("read_password"), getInputArgumentString("write_password"), getInputArgumentString("multi_password"));
        execute(pigfunc, requestor, response);
        return response;
    }
