    private File createTempPackageFile(String filePath) {
        File tmpPackageFile = getFileStreamPath(TMP_FILE_NAME);
        if (tmpPackageFile == null) {
            Log.w(TAG, "Failed to create temp file");
            return null;
        }
        if (tmpPackageFile.exists()) {
            tmpPackageFile.delete();
        }
        FileOutputStream fos;
        try {
            fos = openFileOutput(TMP_FILE_NAME, MODE_WORLD_READABLE);
        } catch (FileNotFoundException e1) {
            Log.e(TAG, "Error opening file " + TMP_FILE_NAME);
            return null;
        }
        try {
            fos.close();
        } catch (IOException e) {
            Log.e(TAG, "Error opening file " + TMP_FILE_NAME);
            return null;
        }
        File srcPackageFile = new File(filePath);
        if (!FileUtils.copyFile(srcPackageFile, tmpPackageFile)) {
            Log.w(TAG, "Failed to make copy of file: " + srcPackageFile);
            return null;
        }
        return tmpPackageFile;
    }
