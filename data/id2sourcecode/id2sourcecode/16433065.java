    @Override
    public void copyImage(ImageObject image, String outputDir) {
        File sourceFile = new File(image.getPath());
        File targetDir = new File(outputDir + "/" + IMAGEFOLDER);
        try {
            FileUtils.copyFileToDirectory(sourceFile, targetDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
