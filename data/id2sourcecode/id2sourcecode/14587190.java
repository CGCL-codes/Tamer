    private final File copyFile(File namedCfgPath, String name, String prefix, File targetPath) throws IOException {
        File file = new File(namedCfgPath, name);
        if (!file.exists()) copyFileStream(prefix + name, file);
        if (file.exists() && targetPath != null) FileUtils.copyFile(file, new File(targetPath, name));
        return file;
    }
