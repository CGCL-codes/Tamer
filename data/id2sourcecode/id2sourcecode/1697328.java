    private static void generateConfigHtml(File srcDir, final File targetDir) throws Exception {
        if (srcDir.isDirectory()) {
            FileSystemUtil.navigateFilesystem(srcDir, new IHandleFile() {

                public void handleFile(File file) throws Exception {
                    if (file.getAbsolutePath().endsWith("xls")) {
                        if (new File(file.getAbsolutePath().replace(".xls", ".csv")).exists()) {
                            String newFileName = file.getName().substring(3).replace(" ", "_").toLowerCase();
                            FileUtils.copyFile(file, new File(targetDir.getAbsolutePath() + "/" + newFileName));
                            FileUtils.copyFile(new File(file.getAbsolutePath().replace(".xls", ".csv")), new File(targetDir.getAbsolutePath() + "/" + newFileName.replace(".xls", ".csv")));
                            String line = "<p><b>" + file.getName().substring(3).replace(".xls", "") + " &nbsp; </b>" + "<link href=\"" + newFileName + "\">Excel Format</link> " + "&nbsp; &nbsp;<link href=\"" + newFileName.replace(".xls", ".csv") + "\">CSV Format</link></p>";
                            System.out.println(line);
                        }
                    }
                }

                public boolean isValidDir(File fileDir) throws Exception {
                    return true;
                }
            });
        }
    }
