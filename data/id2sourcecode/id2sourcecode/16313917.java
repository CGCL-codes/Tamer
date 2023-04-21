    public static void main(String args[]) {
        final String argv[] = args;
        try {
            lck = new FileOutputStream(System.getProperty("java.io.tmpdir") + File.separator + "popeye.lock").getChannel().tryLock();
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot create lock file. Will continue nevertheless.");
        } catch (IOException ex) {
            System.err.println("Cannot create lock file. Will continue nevertheless.");
        }
        if (lck == null) {
            JOptionPane.showMessageDialog(null, "Another instance of popeye was detected running. Close that one first.", "Popeye", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        splash = new SplashScreen(new ImageIcon(ApplicationFramework.class.getResource("/eu/popeye/resources/splashScreen.jpg")));
        splash.setLocationRelativeTo(null);
        splash.setProgressMax(100);
        splash.setScreenVisible(true);
        ApplicationFramework.getInstance().MainbarVisible(true);
    }
