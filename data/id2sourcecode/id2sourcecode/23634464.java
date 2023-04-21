    private static void copyFiles(File directory, IAlgorithmDescription[] descr) throws IOException {
        File programDirectory = JSelectDirectoryDialog.getProgramDirectory();
        for (IAlgorithmDescription des : descr) {
            if (des instanceof RemoteAlgorithmDescription) {
                Bonesa.saveToFile(((RemoteAlgorithmDescription) des).getRoot(), directory.getCanonicalPath() + File.separator + Utils.convertNameToSaveName(des.getName()) + ".bda");
            } else {
                Bonesa.saveToFile(des, directory.getCanonicalPath() + File.separator + Utils.convertNameToSaveName(des.getName()) + ".bda");
            }
        }
        FileUtils.copyFileToDirectory(new File(programDirectory.getCanonicalPath() + File.separator + "ParameterTuning.jar"), directory);
        FileUtils.copyFileToDirectory(new File(programDirectory.getCanonicalPath() + File.separator + "SATINDistribution.jar"), directory);
        FileUtils.copyFileToDirectory(Bonesa.getParameterTunerJar(), new File(directory.getCanonicalPath() + File.separator + "plugins"));
        FileUtils.copyFileToDirectory(new File(programDirectory.getCanonicalPath() + File.separator + "log4j.properties"), directory);
        FileUtils.copyDirectoryToDirectory(new File(programDirectory.getCanonicalPath() + File.separator + "lib"), directory);
        if (new File(programDirectory.getCanonicalPath() + File.separator + "runtime").exists()) {
            FileUtils.copyDirectoryToDirectory(new File(programDirectory.getCanonicalPath() + File.separator + "runtime"), directory);
        }
    }
