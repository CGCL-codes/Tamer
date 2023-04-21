    protected void showUpdateModification(final String _version) {
        InputStream in = null;
        LineNumberReader reader = null;
        try {
            final String lang = CtuluLib.isFrenchLanguageSelected() ? "fr" : "en";
            final URL url = new URL("http://www.fudaa.fr/prepro/inc.last-changelog." + lang + ".html");
            final URLConnection connection = url.openConnection();
            for (int i = 0; i < nbTry_ && in == null; i++) {
                connection.connect();
                in = url.openStream();
                try {
                    if (in == null) {
                        Thread.sleep(wait_);
                    }
                } catch (final InterruptedException _evt) {
                }
            }
            if (in == null) {
                BuBrowserControl.displayURL(updateURL_);
            } else {
                final StringBuffer buf = new StringBuffer(300);
                buf.append("<html><body>");
                reader = new LineNumberReader(new InputStreamReader(in));
                String line = reader.readLine();
                while (line != null) {
                    buf.append(line);
                    line = reader.readLine();
                }
                buf.append("</body></html>");
                CtuluLibDialog.showMessage(getFrame(), TrResource.getS("Modifications apport�es � la version {0}", _version), buf.toString());
            }
        } catch (final IOException _evt) {
        } finally {
            FuLib.safeClose(in);
            FuLib.safeClose(reader);
        }
    }
