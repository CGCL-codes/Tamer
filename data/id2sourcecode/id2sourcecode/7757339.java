    private int countHeight(IContext context) {
        ActionScript a = new ActionScript();
        a.setType(ActionFactory.MAXPAGEACTION);
        a.setPara("delay", "2000");
        ActionFactory.createAction(a).execute(context);
        BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, 10, 350));
        int count = 0;
        int height = 0;
        int temp = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            int rgb = image.getRGB(5, i);
            if (rgb == temp) {
                count++;
            } else {
                temp = rgb;
                count = 0;
                height = i;
            }
            if (count == 100) {
                break;
            }
        }
        robot.delay(Util.getDelayTime(Util.DELAY300));
        return height;
    }
