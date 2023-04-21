    private static void distOSProject(Project pro, OS os, BuildConfig buildConfig) {
        org.apache.tools.ant.Project antProj = new org.apache.tools.ant.Project();
        String proName = pro.getName();
        String osDir;
        if (os == OS.MacOSX) osDir = "dist/os/" + os.id + "/" + proName + "/" + buildConfig.getPrefix() + proName + ".app/Contents/Resources/Java"; else osDir = "dist/os/" + os.id + "/" + proName;
        new File(osDir).mkdirs();
        copyFileOrDir("dist/share/" + proName, osDir);
        copyFileOrDir(proName + "/launcher/dist/" + os.id, osDir);
        File nativeLibsDir = new File("lib/native/" + os.id);
        if (nativeLibsDir.exists() && nativeLibsDir.isDirectory()) {
            for (CodeItem item : pro.getAllDependencies().getCodeItems()) {
                if (item instanceof Library) {
                    Library lib = (Library) item;
                    File dir = new File(nativeLibsDir, lib.getName());
                    if (dir.exists() && dir.isDirectory()) {
                        String projectNativeLibDir = osDir + "/lib/native/" + os.id + "/" + lib.getName();
                        if (!new File(projectNativeLibDir).exists()) new File(projectNativeLibDir).mkdirs();
                        for (File file : dir.listFiles()) {
                            copyFile(file.getAbsolutePath(), projectNativeLibDir + "/" + file.getName());
                        }
                    }
                }
            }
        }
        String packageFileNameWithoutExtension = "dist/packages/" + buildConfig.getPrefix() + proName + "-" + buildConfig.getVersion() + "-" + os.id;
        if (os.isWindows()) {
            Zip zip = new Zip();
            zip.setProject(antProj);
            zip.setDestFile(new File(packageFileNameWithoutExtension + ".zip"));
            zip.setBasedir(new File(osDir));
            zip.execute();
        } else {
            String executableFiles;
            if (os.isMacOSX()) executableFiles = "**/JavaApplicationStub"; else executableFiles = "**/*.sh";
            createBzip2(osDir, executableFiles, packageFileNameWithoutExtension + ".tar.bz2", excludes());
        }
    }
