    public static String[] getReferenceFileNames(String configFile) throws IOException {
        String[] referenceFiles = null;
        LineReader reader = new LineReader(configFile);
        try {
            for (String line : reader) {
                line = line.trim();
                if (Regex.commentOrEmptyLine.matches(line)) continue;
                if (line.indexOf("=") == -1) {
                    String[] fds = Regex.spaces.split(line);
                    if ("oracle".equals(fds[0]) && fds.length >= 3) {
                        referenceFiles = new String[fds.length - 2];
                        for (int i = 0; i < referenceFiles.length; i++) referenceFiles[i] = fds[i + 1].trim();
                    }
                }
            }
        } finally {
            reader.close();
        }
        return referenceFiles;
    }
