    public static AnnotationInfoDataset createAnnotationInfoDatasetFromDefault(int revision) throws IOException, PsiLoaderException {
        URL url = new URL(PSI_MI_LOCAL_ANNOTATIONS + "?revision=" + revision);
        return createAnnotationInfoDatasetFromResource(url.openStream());
    }
