    public static List digestPeriodicsFactory(Element root) {
        ArrayList periodics = new ArrayList();
        for (Iterator i = root.getChildren().iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            Periodic periodic = null;
            if (child.getName().equals("DailyPeriodic")) {
                periodic = new DailyPeriodic();
            } else if (child.getName().equals("WeeklyPeriodic")) {
                periodic = new WeeklyPeriodic();
            }
            if (periodic != null) {
                periodic.digest(child);
                periodics.add(periodic);
            }
        }
        return periodics;
    }
