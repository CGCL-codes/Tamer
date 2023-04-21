    public Response execute() throws org.json.JSONException {
        com.softwoehr.pigiron.functions.ImageDiskCreateDM pigfunc = new com.softwoehr.pigiron.functions.ImageDiskCreateDM(getHostSpecifier(), host.getPortNumber(), user.getUid(), user.getPassword(), getTargetIdentifier(), getInputArgumentString("image_disk_number"), getInputArgumentString("image_disk_device_type"), getInputArgumentString("image_disk_allocation_type"), getInputArgumentString("allocation_area_name_or_volser"), (int) getInputArgumentLong("allocation_unit_size"), (int) getInputArgumentLong("image_disk_size"), getInputArgumentString("image_disk_mode"), (int) getInputArgumentLong("image_disk_formatting"), getInputArgumentString("image_disk_label"), getInputArgumentString("read_password"), getInputArgumentString("write_password"), getInputArgumentString("multi_password"));
        execute(pigfunc, requestor, response);
        return response;
    }
