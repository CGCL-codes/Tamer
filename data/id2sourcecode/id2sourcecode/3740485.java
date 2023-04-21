    private String fetchURLComposeExternPackageList(String urlpath, String pkglisturlpath) {
        String link = pkglisturlpath + "package-list";
        try {
            readPackageList((new URL(link)).openStream(), urlpath, false);
        } catch (MalformedURLException exc) {
            return configuration.getText("doclet.MalformedURL", link);
        } catch (IOException exc) {
            return configuration.getText("doclet.URL_error", link);
        }
        return null;
    }
