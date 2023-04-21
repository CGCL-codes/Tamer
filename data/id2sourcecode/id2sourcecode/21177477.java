    protected void addToZip(ZipOutputStream zos, Resource file, String path, String attributeId, User user) throws Exception {
        if (file != null) {
            if (file.getTypeId() == 5) {
                BinaryAttribute ba = resourceManager.readAttribute(file.getResourceId(), attributeId, user);
                addZipEntry(path, file.getName(), file.getUpdateDate().getTime(), zos);
                int read = 0;
                byte[] readBuffer = new byte[2156];
                InputStream is = ba.getInputStream();
                while ((read = is.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, read);
                }
                is.close();
                zos.closeEntry();
            } else if (file.getTypeId() == 8) {
            }
            List<Resource> dir = resourceManager.getList(file.getResourceId(), user);
            path += toUsAscii(file.getName()) + "/";
            for (Resource resource : dir) {
                addToZip(zos, resource, path, attributeId, user);
            }
        }
    }
