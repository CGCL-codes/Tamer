    private void createApp(String projectName) throws IOException {
        System.out.println("creating " + projectName + "...");
        System.setProperty("user.dir", Config.getProperty("test.out"));
        File dir = new File(Utility.getCurrentDir() + "/" + projectName);
        if (dir.exists()) try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        execute("create-app", projectName, "org.groupId", "org.groupId." + projectName, "versionId", "maven2");
        FileUtils.copyFile(new File(Config.getHome() + "/src/test/EmbededSample.txt"), new File(Utility.getCurrentDir() + "/" + projectName + "/src/main/java/org/groupId/" + projectName + "/models/EmbededSample.java"));
        FileUtils.copyFile(new File(Config.getHome() + "/src/test/${modelName}.txt"), new File(Utility.getCurrentDir() + "/" + projectName + "/generator/model/src_java/${basePackagePath}/models/${subPackagePath}/${modelName}.java"));
    }
