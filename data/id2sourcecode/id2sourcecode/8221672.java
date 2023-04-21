    protected static void runScript(ApplicationModel model, String script, String[] argv) throws IOException {
        if (argv == null) argv = new String[] { "" };
        PySystemState.initialize(PySystemState.getBaseProperties(), null, argv);
        PythonInterpreter interp = JythonUtil.createInterpreter(true, false, model.getDocumentModel(), new NullProgressMonitor());
        System.err.println();
        interp.exec("params=dict()");
        int iargv = -1;
        for (String s : argv) {
            int ieq = s.indexOf("=");
            if (ieq > 0) {
                String snam = s.substring(0, ieq).trim();
                if (DataSourceUtil.isJavaIdentifier(snam)) {
                    String sval = s.substring(ieq + 1).trim();
                    interp.exec("params['" + snam + "']='" + sval + "'");
                } else {
                    if (snam.startsWith("-")) {
                        System.err.println("script arguments should not start with -, they should be name=value");
                    }
                    System.err.println("bad parameter: " + snam);
                }
            } else {
                if (iargv >= 0) {
                    interp.exec("params['arg_" + iargv + "']='" + s + "'");
                    iargv++;
                } else {
                    iargv++;
                }
            }
        }
        URL url = DataSetURI.getURL(script);
        InputStream in = url.openStream();
        interp.execfile(in);
        in.close();
    }
