    private static void replaceOldPackageNames(File file) throws FileNotFoundException, IOException {
        Assert.isTrue(file.isFile(), "Expected plain file");
        LOGGER.info("fix: " + file);
        byte[] result = FileCopyUtils.copyToByteArray(new StreamReplacer(new FileInputStream(file), "/com/adv/".getBytes(), "/ru/adv/".getBytes()));
        result = FileCopyUtils.copyToByteArray(new StreamReplacer(new ByteArrayInputStream(result), "com.adv.".getBytes(), "ru.adv.".getBytes()));
        FileCopyUtils.copy(result, file);
    }
