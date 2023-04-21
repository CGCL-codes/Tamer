    public ProductImage buildProductImage(File uploadProductImageFile) {
        SystemConfig systemConfig = SystemConfigUtil.getSystemConfig();
        String sourceProductImageFormatName = ImageUtil.getImageFormatName(uploadProductImageFile);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        String dateString = simpleDateFormat.format(new Date());
        String uuid = CommonUtil.getUUID();
        String sourceProductImagePath = SystemConfig.UPLOAD_IMAGE_DIR + dateString + "/" + uuid + "." + sourceProductImageFormatName;
        String bigProductImagePath = SystemConfig.UPLOAD_IMAGE_DIR + dateString + "/" + uuid + ProductImage.BIG_PRODUCT_IMAGE_FILE_NAME_SUFFIX + "." + ProductImage.PRODUCT_IMAGE_FILE_EXTENSION;
        String smallProductImagePath = SystemConfig.UPLOAD_IMAGE_DIR + dateString + "/" + uuid + ProductImage.SMALL_PRODUCT_IMAGE_FILE_NAME_SUFFIX + "." + ProductImage.PRODUCT_IMAGE_FILE_EXTENSION;
        String thumbnailProductImagePath = SystemConfig.UPLOAD_IMAGE_DIR + dateString + "/" + uuid + ProductImage.THUMBNAIL_PRODUCT_IMAGE_FILE_NAME_SUFFIX + "." + ProductImage.PRODUCT_IMAGE_FILE_EXTENSION;
        File sourceProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(sourceProductImagePath));
        File bigProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(bigProductImagePath));
        File smallProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(smallProductImagePath));
        File thumbnailProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(thumbnailProductImagePath));
        File watermarkImageFile = new File(ServletActionContext.getServletContext().getRealPath(systemConfig.getWatermarkImagePath()));
        File sourceProductImageParentFile = sourceProductImageFile.getParentFile();
        File bigProductImageParentFile = bigProductImageFile.getParentFile();
        File smallProductImageParentFile = smallProductImageFile.getParentFile();
        File thumbnailProductImageParentFile = thumbnailProductImageFile.getParentFile();
        if (!sourceProductImageParentFile.exists()) {
            sourceProductImageParentFile.mkdirs();
        }
        if (!bigProductImageParentFile.exists()) {
            bigProductImageParentFile.mkdirs();
        }
        if (!smallProductImageParentFile.exists()) {
            smallProductImageParentFile.mkdirs();
        }
        if (!thumbnailProductImageParentFile.exists()) {
            thumbnailProductImageParentFile.mkdirs();
        }
        try {
            BufferedImage srcBufferedImage = ImageIO.read(uploadProductImageFile);
            FileUtils.copyFile(uploadProductImageFile, sourceProductImageFile);
            ImageUtil.zoomAndWatermark(srcBufferedImage, bigProductImageFile, systemConfig.getBigProductImageHeight(), systemConfig.getBigProductImageWidth(), watermarkImageFile, systemConfig.getWatermarkPosition(), systemConfig.getWatermarkAlpha().intValue());
            ImageUtil.zoomAndWatermark(srcBufferedImage, smallProductImageFile, systemConfig.getSmallProductImageHeight(), systemConfig.getSmallProductImageWidth(), watermarkImageFile, systemConfig.getWatermarkPosition(), systemConfig.getWatermarkAlpha().intValue());
            ImageUtil.zoom(srcBufferedImage, thumbnailProductImageFile, systemConfig.getThumbnailProductImageHeight(), systemConfig.getThumbnailProductImageWidth());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProductImage productImage = new ProductImage();
        productImage.setId(uuid);
        productImage.setSourceProductImagePath(sourceProductImagePath);
        productImage.setBigProductImagePath(bigProductImagePath);
        productImage.setSmallProductImagePath(smallProductImagePath);
        productImage.setThumbnailProductImagePath(thumbnailProductImagePath);
        return productImage;
    }
