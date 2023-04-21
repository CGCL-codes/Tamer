    public static void upgrade(File wrFile) throws Exception {
        System.out.println("Installing/upgrading Myna in '" + wrFile.toString() + "'...");
        wrFile.mkdirs();
        File web_inf = new File(wrFile.toURI().resolve("WEB-INF"));
        boolean isUpgrade = false;
        File backupDir = null;
        if (web_inf.exists()) {
            String dateString = new java.text.SimpleDateFormat("MM-dd-yyyy_HH.mm.ss.S").format(new Date());
            String backupBase = "WEB-INF/upgrade_backups/backup_" + dateString;
            backupDir = new File(wrFile.toURI().resolve(backupBase));
            backupDir.mkdirs();
            isUpgrade = true;
            System.out.println("Backups stored in " + backupDir);
        }
        if (isJar) {
            String jarFilePath = classUrl.substring(classUrl.indexOf(":") + 1, classUrl.indexOf("!"));
            File jarFile = new File(new java.net.URL(jarFilePath).toURI());
            ZipFile zipFile = new ZipFile(jarFile);
            for (ZipEntry entry : java.util.Collections.list(zipFile.entries())) {
                ;
                File outputFile = new File(wrFile.toURI().resolve(java.net.URLEncoder.encode(entry.getName(), "UTF-8")));
                File backupFile = null;
                if (isUpgrade) {
                    backupFile = new File(backupDir.toURI().resolve(java.net.URLEncoder.encode(entry.getName(), "UTF-8")));
                }
                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                    if (isUpgrade) backupFile.mkdirs();
                } else {
                    if (isUpgrade && outputFile.exists()) {
                        java.io.InputStream sourceIS = zipFile.getInputStream(entry);
                        java.io.InputStream targetIS = FileUtils.openInputStream(outputFile);
                        boolean isSame = IOUtils.contentEquals(sourceIS, targetIS);
                        sourceIS.close();
                        targetIS.close();
                        if (isSame || entry.toString().equals("index.html") || entry.toString().equals("application.sjs") || entry.toString().equals("WEB-INF/classes/general.properties") || entry.toString().startsWith("WEB-INF/myna/ds")) {
                            continue;
                        } else {
                            System.out.println("...backing up " + entry);
                            FileUtils.copyFile(outputFile, backupFile, true);
                        }
                    }
                    java.io.InputStream is = zipFile.getInputStream(entry);
                    java.io.OutputStream os = FileUtils.openOutputStream(outputFile);
                    IOUtils.copyLarge(is, os);
                    is.close();
                    os.close();
                }
            }
            zipFile.close();
            System.out.println("Done unpacking.");
        }
    }
