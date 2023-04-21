    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Zwei Argumente ben�tigt: <boxName> <timer-dir>");
            System.err.println("Wobei die timer-Dir folgendes enhalten sollte:");
            System.err.println(" - Verzeichnis .timer (z.B. file:/D:/temp/cm/saved-timer)");
            System.err.println(" - das File RA_FILE");
            System.err.println(" - das File SM_FILE");
            System.exit(-1);
        }
        String boxName = args[0];
        String dir = args[1];
        Settings.init(args);
        List cridFiles = TimerUtil.readTimer(LocationFactory.create(dir + "/.timer"));
        List timers = TimerUtil.createTimerList(dir, cridFiles);
        Collections.sort(cridFiles, new Comparator() {

            public int compare(Object o1, Object o2) {
                CridInfo t1 = (CridInfo) o1;
                CridInfo t2 = (CridInfo) o2;
                return t1.toString().compareToIgnoreCase(t2.toString());
            }
        });
        BoxManager boxManager = (BoxManager) ServiceProvider.instance().getService(TimerViewStart.class, BoxManager.class);
        IBox box = boxManager.getBoxByBoxName(boxName);
        if (box == null) {
            System.err.println("Box nicht gefunden: " + boxName);
            System.err.println("Folgende Boxen gibt es:");
            IBox[] boxes = boxManager.getBoxes();
            for (int i = 0; i < boxes.length; i++) {
                System.err.println("  " + boxes[i].getName());
            }
            System.exit(-1);
        }
        ChannelManager channelManager = box.getChannelManager();
        TimerViewTool gui = new TimerViewTool(timers, cridFiles, channelManager, TimerUtil.getInfo());
        gui.setVisible(true);
    }
