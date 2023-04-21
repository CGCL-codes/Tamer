    private static final String getTileHash(ImageRecord r, DjatokaDecodeParam params) throws Exception {
        String id = r.getIdentifier();
        int level = params.getLevel();
        String region = params.getRegion();
        int rotateDegree = params.getRotationDegree();
        double scalingFactor = params.getScalingFactor();
        int[] scalingDims = params.getScalingDimensions();
        String scale = "";
        if (scalingDims != null && scalingDims.length == 1) scale = scalingDims[0] + "";
        if (scalingDims != null && scalingDims.length == 2) scale = scalingDims[0] + "," + scalingDims[1];
        int clayer = params.getCompositingLayer();
        String rft_id = id + "|" + level + "|" + region + "|" + rotateDegree + "|" + scalingFactor + "|" + scale + "|" + clayer;
        MessageDigest complete = MessageDigest.getInstance("SHA1");
        return new String(complete.digest(rft_id.getBytes()));
    }
