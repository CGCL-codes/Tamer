    private static void replaceStringInSecurityFilterConfigs(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.getName().endsWith(".xml")) {
                LOGGER.info("fix: " + file);
                byte[] result = FileCopyUtils.copyToByteArray(new StreamReplacer(new FileInputStream(file), "jdbc:postgresql://tandem2.adv.ru/".getBytes(), "jdbc:postgresql://localhost/".getBytes()));
                FileCopyUtils.copy(result, file);
            }
        }
    }
