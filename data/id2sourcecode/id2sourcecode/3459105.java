    public static boolean copyFileStructure(File sourceDirectory, File targetDirectory, boolean withSubDirectories, StringBuffer errorLog) {
        if (!sourceDirectory.isDirectory()) return false;
        if (!targetDirectory.isDirectory()) return false;
        abortCopy = false;
        abortSuccessful = false;
        boolean result = true;
        File[] srcDirContent = sourceDirectory.listFiles();
        for (int i = 0; i < srcDirContent.length; i++) {
            if (abortCopy) return false;
            if (!result) return false;
            result = result & copyFileStructureRecursive(srcDirContent[i], targetDirectory, withSubDirectories, errorLog);
        }
        abortSuccessful = true;
        return result;
    }
