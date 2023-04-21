            private void copySutToTestsFolder(File sutFile) {
                File classDir = new File(JSystemProperties.getCurrentTestsPath());
                if (sutFile.getAbsolutePath().startsWith(classDir.getAbsolutePath())) {
                    File sutSrcFile = new File(JSystemProperties.getInstance().getPreference(FrameworkOptions.TESTS_SOURCE_FOLDER) + sutFile.getAbsolutePath().substring(classDir.getAbsolutePath().length()));
                    try {
                        FileUtils.copyFile(sutFile, sutSrcFile);
                    } catch (Exception e) {
                        log.log(Level.SEVERE, "Failed updating SUT file", e);
                    }
                }
            }
