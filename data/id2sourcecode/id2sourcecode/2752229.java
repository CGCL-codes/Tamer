    private void exportZipEntry(String path, ZipOutputStream zout) throws Exception {
        WebDavStore file = new WebDavStore(path);
        if (!file.isCollection()) {
            if (path.startsWith("/")) path = path.substring(1);
            ZipEntry zEntry = new ZipEntry(path);
            zout.putNextEntry(zEntry);
            file.readContentAsStream(zout, false);
        } else {
            Vector list = file.listBasic();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                exportZipEntry((path.lastIndexOf("/") == path.length() ? path : path + "/") + ((String[]) it.next())[0], zout);
            }
        }
    }
