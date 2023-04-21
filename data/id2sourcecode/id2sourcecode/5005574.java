    private static void copyInputFileToRepoDir(String file) throws Exception {
        FileUtils.copyFile(new File("./input", file), new File(repoDir, "products.zip"));
    }
