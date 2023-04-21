        private static void m2package(File buildRepo, Map<File, Model> modules, File targetRepo) throws IOException {
            for (Model model : modules.values()) {
                final String suffix = String.format("%s/%s/%s", model.getGroupId().replace('.', File.separatorChar), model.getArtifactId(), model.getVersion());
                final File buildDir = new File(buildRepo, suffix);
                final File targetDir = new File(targetRepo, suffix);
                final File[] files = buildDir.listFiles(new FileFilter() {

                    public boolean accept(File file) {
                        final String name = file.getName();
                        if (file.isDirectory()) return false;
                        if (name.startsWith("maven-metadata") && name.endsWith(".xml")) return false;
                        return true;
                    }
                });
                for (File file : files) {
                    final File targetFile = new File(targetDir, file.getName());
                    System.out.println(String.format("cp %s %s", file, targetFile));
                    FileUtils.copyFile(file, targetFile, true);
                }
            }
        }
