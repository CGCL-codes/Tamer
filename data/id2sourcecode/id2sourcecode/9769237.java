    public void generate(Writer writer, Model model) throws IOException {
        writer.write("/* \r\n * $Id: Application.java 49 2009-11-25 11:10:08Z tlh2000 $\r\n * \r\n * ");
        writer.write(model.getProjectName());
        writer.write("\r\n * ");
        writer.write(model.getCopyright());
        writer.write("\r\n * ");
        writer.write(model.getLegal());
        writer.write("\r\n */\r\npackage ");
        writer.write(model.getPackageNameSwing(""));
        writer.write(";\r\n\r\nimport java.awt.Color;\r\nimport java.awt.event.ActionEvent;\r\nimport java.awt.event.KeyEvent;\r\nimport java.util.Properties;\r\n\r\nimport javax.swing.*;\r\n\r\nimport de.carus.cjfc.tfc.dto.TMCID;\r\nimport de.carus.cjfc.tfc.pr.swing.TMVSystem;\r\nimport de.carus.cjfc.tfc.pr.swing.app.TMVApplication;\r\nimport de.carus.cjfc.tfc.pr.swing.factory.TMApplicationViewFactory;\r\nimport de.carus.cjfc.tfc.pr.swing.frame.TMVMainMenu;\r\nimport de.carus.cjfc.tfc.pr.swing.task.TMVApplicationTask;\r\nimport de.carus.cjfc.tfc.pr.swing.task.TMVTask;\r\nimport de.carus.cjfc.tfc.pr.swing.view.TMVIcons;\r\nimport de.carus.cjfc.tfc.pr.swing.view.TMVUseCase;\r\nimport de.carus.cjfc.tfc.sys.TMSystem;\r\nimport de.carus.cjfc.tfc.util.*;\r\nimport de.carus.cjfc.tfc.val.TMValueException;\r\nimport ");
        writer.write(model.getPackageNameGL());
        writer.write(".");
        writer.write(model.getSystem());
        writer.write(";\r\n\r\n/**\r\n * Swing Hauptapplikation.\r\n * \r\n * @author ");
        writer.write(model.getUser());
        writer.write("\r\n */\r\npublic class ");
        writer.write(model.applicationName);
        writer.write(" extends TMVApplication {\r\n");
        if (model.isSuppressUnusedLoggerWarning()) {
            writer.write("  @SuppressWarnings(\"unused\")\r\n");
        }
        writer.write("  private static final TMLogger logger = new TMLogger(");
        writer.write(model.applicationName);
        writer.write(".class);\r\n  \r\n  public static Icon ICON_WATERMARK = new ImageIcon(");
        writer.write(model.applicationName);
        writer.write(".class.getResource(\"/de/carus/cjfc/tfc/pr/swing/res/Watermark.png\"));\r\n  //static public final TMActionID idAuftragGroup = new TMActionID(\"Verkauf\", \"/de/rrr/jwawi/verkauf/pr/swing/res/Verkauf\" + sizePostfix);\r\n  \r\n  static {\r\n    TMStaticInitializer.init(");
        writer.write(model.applicationName);
        writer.write(".class);\r\n  }\r\n  \r\n  public static Action aboutAction = new AbstractAction(\"Über ");
        writer.write(model.applicationName);
        writer.write("...\", TMVIcons.HELP.getMedium()) {\r\n    public void actionPerformed(ActionEvent e) {\r\n      CVAboutBox dlg = new CVAboutBox(TMVApplicationTask.instance().getFrame(), true);\r\n      dlg.show();\r\n    }\r\n  };\r\n\r\n  public static class Factory extends TMApplicationViewFactory {\r\n    public Factory() {\r\n      super(");
        writer.write(model.getSystem());
        writer.write(".");
        writer.write(model.applicationName);
        writer.write(", null);\r\n    }\r\n\r\n    public TMVMainMenu createMainMenu(TMVTask task) {\r\n      TMVMainMenu menu = new TMVMainMenu(task);\r\n      menu.addNavigation();\r\n\r\n      JButton starter = new JButton(CVStarter.actionStarter);\r\n      starter.setToolTipText(\"Öffnet den Startdialog\");\r\n      starter.setMnemonic(KeyEvent.VK_F5);\r\n      task.getView().addActionAdapter(CVStarter.actionStarter, starter).setComponentOwner(null);\r\n      menu.addToToolBar(starter);\r\n\r\n      JButton btAbout = new JButton(aboutAction);\r\n      JMenuItem menuAbout = new JMenuItem(aboutAction);\r\n      menu.getHelpMenu().add(menuAbout);\r\n      btAbout.setText(\"\");\r\n      menu.getToolBar().add(btAbout);\r\n      return menu;\r\n    }\r\n    \r\n    protected void onAddToTaskGroup( TMActionGroup taskMap ){\r\n      //{{TASK-MAP\r\n      //}}TASK-MAP\r\n      // taskMap.add( false,groupStamm );\r\n    }\r\n  }\r\n  \r\n  public void startApplication(TMCID cidAppUseCase) {\r\n    super.startApplication(cidAppUseCase);\r\n    TMVUseCase.setGlobalWatermark(ICON_WATERMARK);\r\n    TMVUseCase.setGlobalGradientBack( new Color( 204,204,208 ), new Color( 255,255,255) );\r\n  }\r\n\r\n  public boolean startTask(String name, Properties properties) throws StartTaskException {\r\n    return super.startTask(name, properties);\r\n  }\r\n\r\n  protected void setUIdefaults() {\r\n    super.setUIdefaults();\r\n    UIDefaults uiDefaults = UIManager.getDefaults();\r\n    uiDefaults.put(\"TMVFrame.DefaultWidth\", new Integer(1000));\r\n    uiDefaults.put(\"TMVFrame.DefaultHeight\", new Integer(740));\r\n    uiDefaults.put(\"TMVFrame.NavigatorDefaultWidth\", new Integer(250));\r\n  }\r\n\r\n  static public void main(String[] args) {\r\n    TMVApplication swingApp = new ");
        writer.write(model.applicationName);
        writer.write("();\r\n    TMSystem system   = new ");
        writer.write(model.getSystem());
        writer.write("();\r\n    TMVSystem vsystem = new ");
        writer.write(model.getVSystem());
        writer.write("(system);\r\n    swingApp.initRuntime(args, system);\r\n    TMVSystem.initSystem(system,vsystem);\r\n\r\n    // der Activator Thread kann optional aktiviert werden um Kommandos über externe Links zu verarbeiten\r\n    //TMUseCaseActivatorThread starter=new TMUseCaseActivatorThread(swingApp);\r\n    //starter.start();\r\n\r\n    swingApp.startApplication(");
        writer.write(model.getSystem());
        writer.write(".");
        writer.write(model.applicationName);
        writer.write(");\r\n\r\n    String action = System.getProperty(\"cjef.action\");\r\n    if(action != null) {\r\n      executeAction(action);\r\n      return;\r\n    }\r\n\r\n    Runnable dowork = new Runnable() {\r\n      public void run() {\r\n\t\ttry {\r\n\t\t  CVStarter.actionStarter.actionPerformed(null, null, null);\r\n\t\t}\r\n\t\tcatch (TMValueException e) {\r\n\t\t}\r\n      }\r\n    };\r\n    SwingUtilities.invokeLater(dowork);\r\n  }\r\n}");
    }
