    public void setPermissions(String group, boolean browse, boolean read, boolean write, boolean createnew, boolean delete, boolean versioncontrol) {
        iFileServicePermissionsGroup ip = null;
        if (ht.size() > 0) ip = (iFileServicePermissionsGroup) ht.get(group);
        if (ip == null) ip = new iFileServicePermissionsGroup();
        ip.browse = browse;
        ip.read = read;
        ip.write = write;
        ip.createnew = createnew;
        ip.delete = delete;
        ip.versioncontrol = versioncontrol;
        ht.put(group, ip);
    }
