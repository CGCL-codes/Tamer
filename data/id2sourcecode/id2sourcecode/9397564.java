    public static void main(final String args[]) {
        try {
            Threading.disableSingleThreading();
            GLProfile.initSingleton();
        } catch (Throwable t) {
        }
        FrameGrabber.init();
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    myDirectory = new File(MainFrame.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                    if (!myDirectory.isDirectory()) {
                        myDirectory = myDirectory.getParentFile();
                    }
                    String path = myDirectory.getAbsolutePath();
                    String lafClassName = UIManager.getSystemLookAndFeelClassName();
                    ArrayList<String> otherArgs = new ArrayList<String>();
                    for (int i = 0; i < args.length; i++) {
                        if (args[i].equals("--laf") && i + 1 < args.length) {
                            lafClassName = args[i + 1];
                            i++;
                        } else {
                            otherArgs.add(args[i]);
                        }
                    }
                    org.netbeans.swing.plaf.Startup.run(Class.forName(lafClassName), 0, null);
                    String[] searchPath = PropertyEditorManager.getEditorSearchPath();
                    String[] newSearchPath = new String[searchPath.length + 1];
                    newSearchPath[0] = "org.netbeans.beaninfo.editors";
                    System.arraycopy(searchPath, 0, newSearchPath, 1, searchPath.length);
                    PropertyEditorManager.setEditorSearchPath(newSearchPath);
                    PropertyEditorManager.registerEditor(String[].class, StringArrayEditor.class);
                    PropertyEditorManager.registerEditor(double[].class, DoubleArrayEditor.class);
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    MainFrame w = new MainFrame(otherArgs.toArray(new String[0]));
                    w.setLocationByPlatform(true);
                    w.setVisible(true);
                    w.messagesio = new NbIOProvider().getIO("Messages", new Action[0], IOContainer.getDefault());
                    w.messagesio.select();
                } catch (Exception ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, "Could not start ProCamTracker", ex);
                }
            }
        });
    }
