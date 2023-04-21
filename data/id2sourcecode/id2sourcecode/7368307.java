    public void readExifData(boolean overwrite) {
        HashMap tempMap = new HashMap();
        if ((imageFile != null) && ExifReader.isExif(imageFile)) {
            log.debug("Reading EXIF for " + imageFile + " " + overwrite);
            ExifHashMap hashMap = new ExifHashMap(ExifReader.decode(imageFile));
            tempMap.put("exif.cameramake", hashMap.getCameraMake());
            tempMap.put("exif.cameramodel", hashMap.getCameraModel());
            tempMap.put("exif.shutterspeed", hashMap.getShutterSpeed());
            tempMap.put("exif.fstop", hashMap.getFStop());
            tempMap.put("exif.exposureprogram", hashMap.getExposureProgram());
            tempMap.put("exif.flash", hashMap.getFlash());
            tempMap.put("exif.iso", "" + hashMap.getISO());
            tempMap.put("exif.lightsource", hashMap.getLightSource());
            tempMap.put("exif.focallength", "" + hashMap.getRational(ExifConstants.FOCAL_LENGTH));
            String s = hashMap.getString(ExifConstants.CREATION_DATE);
            if (!s.equals("")) tempMap.put(JIGS_DATE, s);
        }
        if (overwrite) {
            descMap.putAll(tempMap);
        } else {
            tempMap.putAll(descMap);
            descMap = tempMap;
        }
        convertTypes();
    }
