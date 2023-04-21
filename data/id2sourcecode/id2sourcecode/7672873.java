    public void processJavaSourceFiles(File path) {
        if (path.isDirectory()) {
            for (File file : path.listFiles()) {
                processJavaSourceFiles(file);
            }
        } else if (path.isFile() && path.getPath().endsWith(".java") && !path.getPath().contains("net" + File.separator + "sf")) {
            Map<String, List<Map<CopyrightField, String>>> copyrights = getCopyrightInfo(path);
            if (copyrights != null) checkCopyrightStatements(copyrights);
        }
    }
