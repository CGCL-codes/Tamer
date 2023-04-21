    public void listDetails(File root, File destDir) throws IOException {
        long l1 = System.currentTimeMillis();
        destDir.mkdirs();
        for (SackImage image : this.domain.images().list()) if (image.type().get().isDetail()) FileUtils.copyFileToDirectory(new File(root, image.originalName()), destDir);
        System.out.println("Elapsed time=" + (System.currentTimeMillis() - l1) + " ms");
    }
