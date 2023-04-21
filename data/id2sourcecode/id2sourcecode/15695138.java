    public static AnnotationInfoDataset createAnnotationInfoDatasetFromLatestResource() throws IOException {
        URL url = new URL(PSI_MI_LOCAL_ANNOTATIONS);
        return createAnnotationInfoDatasetFromResource(url.openStream());
    }
