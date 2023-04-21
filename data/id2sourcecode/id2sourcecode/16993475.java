    public void handleEvent(final Event event) {
        pageStatus = new Status(IStatus.OK, "not_used", 0, "", null);
        if (event.widget == inputLocationPath) if (null == inputLocationPath.getText()) {
            pageStatus = new Status(IStatus.WARNING, "not_used", 0, "If the Configuration File is empty the Autopilot can not run.", null);
        } else {
            try {
                final String fileName = inputLocationPath.getText();
                if (fileName.toLowerCase().startsWith("http")) {
                    final URL url = new URL(fileName);
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    VORGURLRequest.getResourceData(conn);
                } else {
                    @SuppressWarnings("unused") final FileInputStream testFile = new FileInputStream(fileName);
                }
                updateFileType(fileName);
            } catch (final FileNotFoundException fnfe) {
                pageStatus = new Status(IStatus.ERROR, "not_used", 0, "The Configuration File does not exist. Key a valid .NAV or .XML file. Or a valid URL", null);
            } catch (final MalformedURLException e) {
                pageStatus = new Status(IStatus.ERROR, "not_used", 0, "The URL is not valid or not well constructed", null);
            } catch (final IOException e) {
                pageStatus = new Status(IStatus.ERROR, "not_used", 0, "The URL destination can not be read.", null);
            }
        }
        applyToStatusLine(pageStatus);
        getWizard().getContainer().updateButtons();
    }
