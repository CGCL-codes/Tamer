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
