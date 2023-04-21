    public void loadFileSet(java.net.URL url) {
        if (url == null) return;
        String line = "";
        if (!url.toString().endsWith(".jdv")) return;
        try {
            java.net.URLConnection u = url.openConnection();
            DataInputStream in = new DataInputStream(u.getInputStream());
            line = in.readLine();
            if (!line.equalsIgnoreCase("This file is automatically produced by a viewer")) return;
            in.readLine();
            do {
                line = in.readLine();
                if (line == null) break;
                if (MainClass.isApplet) {
                    int index = line.lastIndexOf("\\");
                    if (index < 0) index = line.lastIndexOf("/");
                    if (index < 0) index = line.lastIndexOf(File.separator);
                    if (index < 0) continue;
                    line = frame.applet.getCodeBase().toString() + line.substring(index + 1);
                    Tools.debug(this, "Line is :" + line);
                }
                if (!OpenOther.openStringURL(line, frame)) continue;
            } while (line != null);
            in.close();
        } catch (Exception ie) {
            tools.Tools.debug("IOE exception " + ie);
        }
    }
