    public boolean execute(IContext context) {
        if (!super.execute(context)) {
            return false;
        }
        String target = action.getPara("target");
        if (Attributes.START.equalsIgnoreCase(target)) {
            ActionScript a = new ActionScript();
            a.setType(ActionFactory.STARTSERVERACTION);
            ActionFactory.createAction(a).execute(context);
            ActionScript b = new ActionScript();
            b.setType(ActionFactory.MAXPAGEACTION);
            ActionFactory.createAction(b).execute(context);
            BufferedImage image = robot.createScreenCapture(new Rectangle(5, 5, 100, 10));
            robot.delay(Util.getDelayTime(Util.DELAY300));
        } else if (Attributes.STOP.equalsIgnoreCase(target)) {
            ActionScript a = new ActionScript();
            a.setType(ActionFactory.STOPSERVERACTION);
            ActionFactory.createAction(a).execute(context);
        }
        return true;
    }
