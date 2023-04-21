    public static String assureResource(String key, String val, File moduleDir) throws IOException {
        File candidate = null;
        File source = null;
        File targetDir = new File(moduleDir, WEB_RESOURCES_LOCATION);
        boolean copySource = true;
        candidate = new File(val);
        if (isResourceCandidate(candidate)) {
            source = candidate;
        }
        if (source == null) {
            candidate = new File(targetDir, val);
            if (isResourceCandidate(candidate)) {
                source = candidate;
                copySource = false;
            }
        }
        if (source == null) {
            candidate = new File(Preferences.getGorillaHome(""), val);
            if (isResourceCandidate(candidate)) {
                source = candidate;
            }
        }
        if (source == null) {
            return null;
        }
        if (source.getParentFile().equals(targetDir)) {
            copySource = false;
        }
        if (copySource) {
            FileUtils.copyFileToDirectory(source, targetDir);
        }
        val = source.getName();
        return val;
    }
