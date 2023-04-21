    private Resource createResource(String mime, boolean attachment) {
        QFile resourceFile;
        resourceFile = new QFile(fileName);
        resourceFile.open(new QIODevice.OpenMode(QIODevice.OpenModeFlag.ReadOnly));
        byte[] fileData = resourceFile.readAll().toByteArray();
        resourceFile.close();
        if (fileData.length == 0) return null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(fileData);
            byte[] hash = md.digest();
            Resource r = new Resource();
            Calendar time = new GregorianCalendar();
            Long l = time.getTimeInMillis();
            long prevTime = l;
            while (l == prevTime) {
                time = new GregorianCalendar();
                l = time.getTimeInMillis();
            }
            r.setGuid(new Long(l).toString());
            r.setNoteGuid(newNote.getGuid());
            r.setMime(mime);
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
            a.setAttachment(attachment);
            a.setAttachmentIsSet(true);
            a.setClientWillIndex(false);
            a.setClientWillIndexIsSet(true);
            a.setRecoType("");
            a.setRecoTypeIsSet(false);
            a.setSourceURLIsSet(false);
            a.setTimestamp(0);
            a.setTimestampIsSet(false);
            a.setFileName(fileInfo.fileName());
            a.setFileNameIsSet(true);
            r.setAttributes(a);
            conn.getNoteTable().noteResourceTable.saveNoteResource(r, true);
            return r;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return null;
    }
