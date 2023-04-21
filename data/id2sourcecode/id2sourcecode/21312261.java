    public static void showSecurityDialog() {
        final String msg = "When running the Java Sound demo as an applet these\n" + "permissions are necessary in order to load/save files\n" + "and record audio :  \n\n" + "grant { \n" + "  permission java.io.FilePermission \"<<ALL FILES>>\", " + "\"read, write\";\n" + "  permission javax.sound.sampled.AudioPermission \"record\";\n" + "  permission java.util.PropertyPermission \"user.dir\", " + "\"read\";\n" + "}; \n\n" + "The permissions need to be added to the .java.policy file.";
        JOptionPane.showMessageDialog(null, msg, "Applet Info", JOptionPane.INFORMATION_MESSAGE);
    }
