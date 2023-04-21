    public static PhotoInfo addToDB(File imgFile) throws PhotoNotFoundException {
        VolumeBase vol = null;
        try {
            vol = VolumeBase.getVolumeOfFile(imgFile);
        } catch (IOException ex) {
            throw new PhotoNotFoundException();
        }
        File instanceFile = null;
        if (vol == null) {
            vol = VolumeBase.getDefaultVolume();
            instanceFile = vol.getFilingFname(imgFile);
            try {
                FileUtils.copyFile(imgFile, instanceFile);
            } catch (IOException ex) {
                log.warn("Error copying file: " + ex.getMessage());
                throw new PhotoNotFoundException();
            }
        } else if (vol instanceof ExternalVolume) {
            instanceFile = imgFile;
        } else if (vol instanceof Volume) {
            throw new PhotoNotFoundException();
        } else {
            throw new java.lang.Error("Unknown subclass of VolumeBase: " + vol.getClass().getName());
        }
        ODMGXAWrapper txw = new ODMGXAWrapper();
        PhotoInfo photo = PhotoInfo.create();
        txw.lock(photo, Transaction.WRITE);
        photo.addInstance(vol, instanceFile, ImageInstance.INSTANCE_TYPE_ORIGINAL);
        photo.setCropBounds(new Rectangle2D.Float(0.0F, 0.0F, 1.0F, 1.0F));
        photo.updateFromOriginalFile();
        txw.commit();
        return photo;
    }
