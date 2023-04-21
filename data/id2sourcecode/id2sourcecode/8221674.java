    public static void invokeScriptSoon(final URL url, final Application dom, ProgressMonitor mon1) {
        final ProgressMonitor mon;
        if (mon1 == null) {
            mon = new NullProgressMonitor();
        } else {
            mon = mon1;
        }
        Runnable run = new Runnable() {

            public void run() {
                try {
                    PythonInterpreter interp = JythonUtil.createInterpreter(true, false, dom, mon);
                    System.err.println("invokeScriptSoon(" + url + ")");
                    interp.execfile(url.openStream(), url.toString());
                    mon.finished();
                } catch (IOException ex) {
                    Logger.getLogger(AutoplotUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        RequestProcessor.invokeLater(run);
    }
