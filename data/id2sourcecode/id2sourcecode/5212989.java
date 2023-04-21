    public void digest(Element root) {
        Element se = root.getChild("start");
        if (se != null) {
            start = new Time();
            start.digest(se);
        } else {
            start = null;
        }
        Element ee = root.getChild("end");
        if (ee != null) {
            end = new Time();
            end.digest(ee);
        } else {
            end = null;
        }
    }
