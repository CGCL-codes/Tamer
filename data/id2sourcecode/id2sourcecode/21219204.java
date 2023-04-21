    public void getUpdateInfo(long now) {
        if (updateURL == null) {
            connectFailed = updateAvailable = false;
        } else try {
            long deltaTime = (lastUpdateCheckTime < 0 ? -1 : now - lastUpdateCheckTime);
            URL url = new URL(updateURL + "?id=" + id + "&ver=" + version + "&time=" + deltaTime);
            URLConnection conn = url.openConnection();
            conn.setAllowUserInteraction(true);
            int cl = conn.getContentLength();
            connectFailed = (cl < 0);
            updateAvailable = (cl > 0);
            if (updateAvailable) {
                try {
                    updateDocument = XMLUtils.parse(conn.getInputStream());
                    connectFailed = false;
                } catch (Exception e) {
                    connectFailed = true;
                    updateAvailable = false;
                }
            }
            if (updateAvailable) {
                updateAvailable = false;
                NodeList updatePackages = updateDocument.getDocumentElement().getElementsByTagName(XML_PKG_TAG);
                int numPackages = updatePackages.getLength();
                Element pkg;
                for (int i = 0; i < numPackages; i++) {
                    if (!(updatePackages.item(i) instanceof Element)) continue;
                    pkg = (Element) updatePackages.item(i);
                    String xmlPackageID = pkg.getAttribute(XML_PKG_ID_ATTR);
                    if (!id.equals(xmlPackageID)) continue;
                    userURL = pkg.getAttribute(XML_PKG_USER_URL_ATTR);
                    String xmlVers = pkg.getAttribute(XML_PKG_VERSION_ATTR);
                    debug("Retrieved XML for package " + id + "\n\tcurrent-version = " + xmlVers + "\n\tuser-url = " + userURL);
                    if (compareVersions(version, xmlVers) < 0) updateAvailable = true;
                    break;
                }
            }
            if (!connectFailed) InternalSettings.set(AutoUpdateManager.AUTO_UPDATE_SETTING + AutoUpdateManager.LAST_CHECK + "." + id, Long.toString(lastUpdateCheckTime = now), COMMENT_START + "\"" + name + "\"");
            debug("getUpdateInfo: for " + name + "\n\tconnectFailed = " + connectFailed + "\n\tupdateAvailable = " + updateAvailable);
        } catch (IOException ioe) {
        }
    }
