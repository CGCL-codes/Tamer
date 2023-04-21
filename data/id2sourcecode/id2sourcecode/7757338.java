    public boolean execute(IContext context) {
        if (!super.execute(context)) {
            return false;
        }
        String filepath = new File("html/blank.html").getAbsolutePath();
        ActionScript a = new ActionScript();
        a.setType(ActionFactory.OPENURLACTION);
        a.setPara("target", filepath);
        ActionFactory.createAction(a).execute(context);
        int height = countHeight(context);
        this.actionContext.getProjectContext().getProject().getBrowser().setBarHeight(height);
        Util.info("set browser bar height:" + height);
        BufferedImage image = robot.createScreenCapture(new Rectangle(5, 5, 10, 10));
        robot.delay(Util.getDelayTime(Util.DELAY300));
        ActionScript b = new ActionScript();
        b.setType(ActionFactory.CLOSEPAGEACTION);
        b.setPara("delay", "1000");
        ActionFactory.createAction(b).execute(context);
        height = countHeight(context);
        this.actionContext.getProjectContext().getProject().getBrowser().setTitlebarHeight(height);
        Util.info("set browser titlebar height:" + height);
        ActionScript c = new ActionScript();
        c.setType(ActionFactory.CLOSEPAGEACTION);
        c.setPara("delay", "1000");
        ActionFactory.createAction(c).execute(context);
        return true;
    }
