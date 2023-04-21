    private void actionErrorList() {
        Object selected = listAllErrors.getSelectedItem();
        if ((selected instanceof CheckError) && (Utilities.isDesktopSupported())) {
            CheckError error = (CheckError) selected;
            String url = "http://toolserver.org/~sk/cgi-bin/checkwiki/checkwiki.cgi" + "?id=" + error.getErrorNumber() + "&project=" + getWikipedia().getSettings().getCodeCheckWiki() + "&view=only" + "&limit=" + modelMaxErrors.getNumber();
            Utilities.browseURL(url);
        }
    }
