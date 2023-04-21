    @Override
    public String toString() {
        String hrefString = super.getAttr("href");
        if (StringUtils.isBlank(hrefString)) throw new IllegalArgumentException("'href' isn't set!");
        if (!isExternalLink) {
            CKFileResource cKFileResource = new CKFileResource(this.pojoHelper.getSite());
            cKFileResource.consructFromTagFromView(hrefString);
            if (isExportView) {
                try {
                    File srcFile = cKFileResource.getFile();
                    File destFile = cKFileResource.getExportFile();
                    FileUtils.copyFile(srcFile, destFile);
                } catch (IOException e) {
                    logger.error("Error while copy [" + cKFileResource.getFile().getPath() + "] to [" + cKFileResource.getExportFile().getPath() + "]: " + e.getMessage(), e);
                    throw new FatalException("Error while copy [" + cKFileResource.getFile().getPath() + "] to [" + cKFileResource.getExportFile().getPath() + "]: " + e.getMessage(), e);
                }
                this.setHref(cKFileResource.getTagSrcForExport(this.pojoHelper.getLevel()));
            } else this.setHref(cKFileResource.getTagSrcForPreview());
        }
        isExternalLink = false;
        return super.contructTag();
    }
