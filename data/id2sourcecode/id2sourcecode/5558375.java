    private Resource createResource(byte[] fileData, String address) {
        logger.log(logger.EXTREME, "Inside create resource");
        MessageDigest md;
        try {
            logger.log(logger.EXTREME, "Generating MD5");
            md = MessageDigest.getInstance("MD5");
            md.update(fileData);
            byte[] hash = md.digest();
            Resource r = new Resource();
            r.setGuid(QUuid.createUuid().toString().replace("}", "").replace("{", ""));
            r.setNoteGuid(noteGuid);
            r.setMime("image/" + address.substring(address.lastIndexOf(".") + 1));
            r.setActive(true);
            r.setUpdateSequenceNum(0);
            r.setWidth((short) 0);
            r.setHeight((short) 0);
            r.setDuration((short) 0);
            Data d = new Data();
            d.setBody(fileData);
            d.setBodyIsSet(true);
            d.setBodyHash(hash);
            d.setBodyHashIsSet(true);
            r.setData(d);
            d.setSize(fileData.length);
            int fileNamePos = address.lastIndexOf(File.separator);
            if (fileNamePos == -1) fileNamePos = address.lastIndexOf("/");
            String fileName = address.substring(fileNamePos + 1);
            ResourceAttributes a = new ResourceAttributes();
            a.setAltitude(0);
            a.setAltitudeIsSet(false);
            a.setLongitude(0);
            a.setLongitudeIsSet(false);
            a.setLatitude(0);
            a.setLatitudeIsSet(false);
            a.setCameraMake("");
            a.setCameraMakeIsSet(false);
            a.setCameraModel("");
            a.setCameraModelIsSet(false);
            a.setAttachment(false);
            a.setAttachmentIsSet(true);
            a.setClientWillIndex(false);
            a.setClientWillIndexIsSet(true);
            a.setRecoType("");
            a.setRecoTypeIsSet(false);
            a.setSourceURL(fileName);
            a.setSourceURLIsSet(true);
            a.setTimestamp(0);
            a.setTimestampIsSet(false);
            a.setFileName(fileName);
            a.setFileNameIsSet(true);
            r.setAttributes(a);
            logger.log(logger.EXTREME, "Resource created");
            return r;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return null;
    }
