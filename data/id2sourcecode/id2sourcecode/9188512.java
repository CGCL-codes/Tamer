    public static String copyFile(final File file, final String targetDirName) {
        System.out.println("Copying file...");
        final String hash = FileUtils.getHashSum(file);
        final List<String> pathArray = new ArrayList<String>(3);
        pathArray.add(hash.substring(0, 2));
        pathArray.add(hash.substring(2, 4));
        final File targetDir = new File(targetDirName + "/" + StringUtils.join(pathArray, "/"));
        pathArray.add(hash.substring(4));
        final File target = new File(targetDirName + "/" + StringUtils.join(pathArray, "/"));
        try {
            targetDir.mkdirs();
            target.createNewFile();
            FileUtils.copyFile(file, target);
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Created file: " + target.getAbsolutePath());
        return StringUtils.join(pathArray, "/");
    }
