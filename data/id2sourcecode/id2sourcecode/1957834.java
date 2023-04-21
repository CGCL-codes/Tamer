    @Override
    public MediaBean getImage(MediaRequestType type, long id, boolean force, SimpleDateFormat sdf, Locale currLocale, String modStr) {
        if (logger.isDebugEnabled()) logger.debug("getImage()");
        Supplier supplier = supplierDao.findById(id);
        if (logger.isDebugEnabled()) logger.debug("Supplier: " + supplier);
        MediaBean mb = new MediaBean();
        if (supplier == null) {
            mb.getCodes().add(new HttpCode(404));
            logger.error(new HttpCode(404));
            return mb;
        }
        String contentType = null;
        byte[] content = null;
        Date lastMod = null;
        contentType = supplier.getPreviewContentType();
        content = supplier.getPreviewContent();
        lastMod = supplier.getPreviewMod();
        mb.setContentType(contentType);
        mb.setContent(content);
        if (!force) if (StringUtils.isNotEmpty(modStr)) {
            Date ims = null;
            try {
                ims = sdf.parse(modStr);
            } catch (ParseException e) {
                logger.error(e, e);
            }
            if (!ims.before(lastMod)) {
                mb.getCodes().add(new HttpCode(304));
                logger.warn(new HttpCode(304));
                return mb;
            }
        }
        mb.setLastMod(sdf.format(lastMod));
        return mb;
    }
