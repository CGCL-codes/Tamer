    public boolean execute(IContext context) {
        if (!super.execute(context)) {
            return false;
        }
        String value = action.getPara("value");
        if (value != null) {
            String[] values = value.split(",");
            if (values == null || values.length != 4) {
                return false;
            }
            int x = Integer.parseInt(values[0]);
            int y = Integer.parseInt(values[1]);
            int width = Integer.parseInt(values[2]);
            int height = Integer.parseInt(values[3]);
            rectangle = new Rectangle(x, y, width, height);
            oldBI = robot.createScreenCapture(rectangle);
            long start = System.currentTimeMillis();
            boolean isEquealed = true;
            long end = System.currentTimeMillis();
            long counts = end - start;
            if (this.getTimeout() != null) {
                long time = Long.valueOf(this.timeout).longValue();
                while (isEquealed && counts < time) {
                    robot.delay(Util.getDelayTime(Util.DELAY500));
                    isEquealed = isEquealed(oldBI);
                    end = System.currentTimeMillis();
                    counts = end - start;
                }
            }
            String delay = action.getPara("delay");
            if (delay != null) {
                robot.delay(Util.getDelayTime(Integer.parseInt(delay)));
            } else {
                robot.delay(Util.getDelayTime(Util.CHECKDELAY));
            }
            return !isEquealed;
        }
        return false;
    }
