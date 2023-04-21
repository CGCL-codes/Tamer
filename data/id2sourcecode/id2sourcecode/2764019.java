    private void writePolicy(FileWriter fw, File testResourceDirectory, File submissionDirectory) throws IOException {
        fw.write("grant codeBase \"file:" + submissionDirectory.getAbsolutePath() + "/-\" {\n");
        fw.write("  permission java.io.FilePermission \"file:" + System.getProperty("java.io.tmpdir") + "\", \"read, write, delete\";");
        fw.write("  permission java.io.FilePermission \"file:" + submissionDirectory.getAbsolutePath() + "\", \"read\";");
        fw.write("  permission java.lang.RuntimePermission \"accessDeclaredMembers\";");
        fw.write("  permission java.util.PropertyPermission \"*\", \"read\";");
    }
