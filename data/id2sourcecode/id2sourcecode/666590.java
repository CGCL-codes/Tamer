    public static void showInfoDialog() {
        final String msg = "When running this program as an applet these permissions\n" + "are necessary in order to load/save files and record audio :  \n\n" + "grant { \n" + "  permission java.io.FilePermission \"<<ALL FILES>>\", \"read, write\";\n" + "  permission javax.sound.sampled.AudioPermission \"record\"; \n" + "  permission java.util.PropertyPermission \"user.dir\", \"read\";\n" + "}; \n\n" + "The permissions need to be added to the .java.policy file.";
        new Thread(new Runnable() {

            public void run() {
                JOptionPane.showMessageDialog(null, msg, "Applet Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }).start();
    }
