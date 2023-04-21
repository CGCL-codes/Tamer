    private AutoplotHelpSystem(Component uiBase) {
        SwingHelpUtilities.setContentViewerUI("org.autoplot.help.AutoplotHelpViewer");
        helpIds = new HashMap<Component, String>();
        URL hsurl;
        try {
            hsurl = getClass().getResource("/helpfiles/autoplotHelp.hs");
            mainHS = new HelpSet(null, hsurl);
        } catch (Exception ex) {
            log.warning("Error loading helpset " + "/helpfiles/autoplotHelp.hs");
        }
        Enumeration<URL> hsurls = null;
        try {
            hsurls = getClass().getClassLoader().getResources("META-INF/helpsets.txt");
        } catch (IOException ex) {
            log.warning(ex.toString());
        }
        while (hsurls != null && hsurls.hasMoreElements()) {
            hsurl = hsurls.nextElement();
            log.log(Level.FINE, "found /META-INF/helpsets.txt at {0}", hsurl);
            BufferedReader read = null;
            try {
                read = new BufferedReader(new InputStreamReader(hsurl.openStream()));
                String spec = read.readLine();
                while (spec != null) {
                    int i = spec.indexOf("#");
                    if (i != -1) {
                        spec = spec.substring(0, i);
                    }
                    spec = spec.trim();
                    if (spec.length() > 0) {
                        URL hsurl1 = null;
                        try {
                            log.log(Level.FINE, "Merging external helpset: {0}", hsurl);
                            if (spec.startsWith("/")) {
                                hsurl1 = getClass().getResource(spec);
                            } else {
                                hsurl1 = new URL(spec);
                            }
                            mainHS.add(new HelpSet(null, hsurl1));
                        } catch (Exception ex) {
                            log.log(Level.WARNING, "Error loading helpset {0}", hsurl1);
                        }
                    }
                    spec = read.readLine();
                }
            } catch (IOException ex) {
                log.warning(ex.toString());
            } finally {
                try {
                    if (read != null) read.close();
                } catch (IOException ex) {
                    log.warning(ex.toString());
                }
            }
        }
    }
