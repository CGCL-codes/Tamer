    public String getFullFileName() {
        String extension = ".bin";
        int captureType = item.getCapType();
        if (captureType == -1) {
            Channel ch = store.getChannel(item.getChannel());
            if (ch != null) captureType = ch.getCaptureType();
        }
        if (captureType == -1) {
            try {
                captureType = Integer.parseInt(store.getProperty("Capture.deftype"));
            } catch (Exception e) {
            }
        }
        CaptureCapability capability = CaptureCapabilities.getInstance().getCapabiltyWithID(captureType);
        if (capability != null) extension = "." + capability.getFileExt();
        long avDataRateSec = 7000000;
        try {
            avDataRateSec = Long.parseLong(store.getProperty("Capture.AverageDataRate").trim());
        } catch (Exception e) {
        }
        avDataRateSec = (long) (((double) (avDataRateSec * 60) / (double) 8) / (double) (1024 * 1024));
        boolean calculateUsage = "1".equals(store.getProperty("Capture.IncludeCalculatedUsage").trim());
        if (calculateUsage) {
            item.log("Using Calculated Usage (" + avDataRateSec + " MB Minute)");
            System.out.println(this + " : Using Calculated Usage (" + avDataRateSec + " MB Minute)");
        } else {
            avDataRateSec = 0;
        }
        long estimatedUsage = item.getDuration() * avDataRateSec;
        String capturePath = determinCapturePath(item.getCapturePathIndex(), estimatedUsage) + File.separator;
        String fileName = item.getFileName();
        File dir = new File(capturePath + fileName + extension);
        int count = 1;
        while (dir.exists()) {
            String tempName = fileName + "-" + count++;
            dir = new File(capturePath + tempName + extension);
        }
        File parentPath = dir.getParentFile();
        if (!parentPath.exists()) {
            parentPath.mkdirs();
        }
        System.out.println(this + " : Capture File Name : " + dir.getAbsolutePath());
        return dir.getAbsolutePath();
    }
