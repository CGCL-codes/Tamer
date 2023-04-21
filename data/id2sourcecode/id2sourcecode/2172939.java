    private void invokeCmd(String cmd) {
        Category log = ThreadCategory.getInstance(Manager.class);
        try {
            URL invoke = new URL("http://127.0.0.1:8181/invoke?objectname=OpenNMS%3AName=FastExit&operation=" + cmd);
            InputStream in = invoke.openStream();
            int ch;
            while ((ch = in.read()) != -1) System.out.write((char) ch);
            in.close();
            System.out.println("");
            System.out.flush();
        } catch (Throwable t) {
            log.error("error invoking " + cmd + " command", t);
        }
    }
