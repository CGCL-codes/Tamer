    private void copyTwoPolicyJar() {
        final String LIB_DIR = "lib" + File.separator;
        final String JAR1 = "local_policy.jar";
        final String JAR2 = "US_export_policy.jar";
        final String sJAVA_SECURITY_LIB = String.format("%s%slib%ssecurity%s", SystemUtils.getJavaHome().getAbsolutePath(), File.separator, File.separator, File.separator, File.separator);
        File JAVA_SECURITY_LIB = new File(sJAVA_SECURITY_LIB);
        if (new File(sJAVA_SECURITY_LIB + JAR1).exists() && new File(sJAVA_SECURITY_LIB + JAR2).exists()) {
            System.out.printf("%s and %s all exist. No need to copy again.\n ", JAR1, JAR2);
            return;
        }
        System.out.println("JAVA_SECURITY_LIB= " + JAVA_SECURITY_LIB.getAbsolutePath());
        System.out.println("JAR1= " + LIB_DIR + JAR1);
        System.out.println("JAR2= " + LIB_DIR + JAR2);
        try {
            FileUtils.copyFileToDirectory(new File(LIB_DIR + JAR1), JAVA_SECURITY_LIB);
            FileUtils.copyFileToDirectory(new File(LIB_DIR + JAR2), JAVA_SECURITY_LIB);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.printf("%s and %s copied to %s.\n ", JAR1, JAR2, sJAVA_SECURITY_LIB);
        JAVA_SECURITY_LIB = null;
    }
