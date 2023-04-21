    public void storeIcons(ZipOutputStream out) {
        if (m_strImagePath == null || (m_strImagePath != null && !m_strImagePath.startsWith("intern://"))) {
            try {
                ImageLoader loader = new ImageLoader();
                loader.data = new ImageData[] { getIcon().getImageData() };
                out.putNextEntry(new ZipEntry("icon.png"));
                loader.save(out, SWT.IMAGE_PNG);
                out.closeEntry();
                loader.data = new ImageData[] { getSmallIcon().getImageData() };
                out.putNextEntry(new ZipEntry("icon-small.png"));
                loader.save(out, SWT.IMAGE_PNG);
                out.closeEntry();
                loader.data = new ImageData[] { getDefaultReflectionImage().getImageData() };
                out.putNextEntry(new ZipEntry("icon-reflection.png"));
                loader.save(out, SWT.IMAGE_PNG);
                out.closeEntry();
            } catch (IOException e) {
            }
        }
    }
