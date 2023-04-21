    public static void main(final String[] args) throws IOException {
        final File thisPropertyFile = new File("../WordNetTransWizard/read-write.properties");
        System.out.println("run entered");
        Launcher.run(false, Test.class.getName(), new String[] { thisPropertyFile.getCanonicalPath() }, null, "/home/bbou/.m2/repository/org/xerial/sqlite-jdbc/3.7.2/sqlite-jdbc-3.7.2.jar");
        System.out.println("run returned");
    }
