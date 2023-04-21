    public static void createGameClass() {
        try {
            String packageDir, srcDir;
            if (bluej != null) {
                packageDir = bluej.getCurrentPackage().getDir().getCanonicalPath() + "/";
                srcDir = packageDir;
            } else {
                packageDir = "";
                srcDir = "src/";
            }
            File gameJavaFile = new File(packageDir + "examples/scenecreator/Game.java");
            File gameJavaFileDest = new File(srcDir + "Game.java");
            File gameObjectJavaFile = new File(packageDir + "examples/scenecreator/GameObject.java");
            File gameObjectJavaFileDest = new File(srcDir + "GameObject.java");
            if (!gameJavaFileDest.isFile()) {
                FileUtils.copyFile(gameJavaFile, gameJavaFileDest);
                if (bluej != null) bluej.getCurrentPackage().newClass("Game");
            }
            if (!gameObjectJavaFileDest.isFile()) {
                FileUtils.copyFile(gameObjectJavaFile, gameObjectJavaFileDest);
                if (bluej != null) bluej.getCurrentPackage().newClass("GameObject");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
