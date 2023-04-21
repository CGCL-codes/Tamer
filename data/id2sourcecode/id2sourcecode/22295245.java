    public Response execute() throws org.json.JSONException {
        com.softwoehr.pigiron.functions.ImageDiskShare pigfunc = new com.softwoehr.pigiron.functions.ImageDiskShare(getHostSpecifier(), host.getPortNumber(), user.getUid(), user.getPassword(), getTargetIdentifier(), getInputArgumentString("image_disk_number"), getInputArgumentString("target_image_name"), getInputArgumentString("target_image_disk_number"), getInputArgumentString("read_write_mode"), getInputArgumentString("optional_password"));
        execute(pigfunc, requestor, response);
        return response;
    }
