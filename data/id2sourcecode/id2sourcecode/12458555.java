    public static void fetch(JaguarRectangle r) {
        try {
            JaguarRectangle s = getRows(r.getName(), robby.createScreenCapture(r));
            setScanlines(r, s);
        } catch (Exception e) {
            e.printStackTrace();
            addLine(e.getMessage());
        }
    }
