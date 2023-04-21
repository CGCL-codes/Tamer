    public void setupWindow(ApplicationContext context, Project currentProject, int clientWidth, int clientHeight) {
        currentProject.setStartupParam("MainClass", "org.formaria.swt.Applet");
        ComponentFactory.setRequiresParent(true);
        currentProject.setStartupParam("DefaultClass", "org.formaria.swt.SwtPage");
        currentProject.setEventHandlerClass("org.formaria.swt.SwtEventHandler");
        clientShell.setSize(clientWidth, clientHeight);
        clientShell.setVisible(true);
        clientShell.setLayout(new RowLayout());
        currentProject.setObject("Applet", this);
        currentProject.setObject("ClientShell", clientShell);
        currentProject.setObject("Display", display);
        String icon = currentProject.getStartupParam("Icon");
        if (icon != null) {
            try {
                InputStream url = currentProject.getUrl(icon).openStream();
                clientShell.setImage(new Image(display, url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        clientShell.open();
    }
