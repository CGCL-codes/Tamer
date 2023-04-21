    protected void parseMetaData(String ctxPath, URL warURL, String warName, WebMetaData metaData) throws DeploymentException {
        InputStream jbossWebIS = null;
        InputStream webIS = null;
        try {
            File warDir = new File(warURL.getFile());
            if (warURL.getProtocol().equals("file") && warDir.isDirectory() == true) {
                File webDD = new File(warDir, "WEB-INF/web.xml");
                if (webDD.exists() == true) webIS = new FileInputStream(webDD);
                File jbossWebDD = new File(warDir, "WEB-INF/jboss-web.xml");
                if (jbossWebDD.exists() == true) jbossWebIS = new FileInputStream(jbossWebDD);
            } else {
                InputStream warIS = warURL.openStream();
                java.util.zip.ZipInputStream zipIS = new java.util.zip.ZipInputStream(warIS);
                java.util.zip.ZipEntry entry;
                byte[] buffer = new byte[512];
                int bytes;
                while ((entry = zipIS.getNextEntry()) != null) {
                    if (entry.getName().equals("WEB-INF/web.xml")) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        while ((bytes = zipIS.read(buffer)) > 0) {
                            baos.write(buffer, 0, bytes);
                        }
                        webIS = new ByteArrayInputStream(baos.toByteArray());
                    } else if (entry.getName().equals("WEB-INF/jboss-web.xml")) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        while ((bytes = zipIS.read(buffer)) > 0) {
                            baos.write(buffer, 0, bytes);
                        }
                        jbossWebIS = new ByteArrayInputStream(baos.toByteArray());
                    }
                }
                zipIS.close();
            }
            XmlFileLoader xmlLoader = new XmlFileLoader();
            String warURI = warURL.toExternalForm();
            try {
                if (webIS != null) {
                    Document webDoc = xmlLoader.getDocument(webIS, warURI + "/WEB-INF/web.xml");
                    Element web = webDoc.getDocumentElement();
                    metaData.importXml(web);
                }
            } catch (Exception e) {
                throw new DeploymentException("Failed to parse WEB-INF/web.xml", e);
            }
            try {
                if (jbossWebIS != null) {
                    Document jbossWebDoc = xmlLoader.getDocument(jbossWebIS, warURI + "/WEB-INF/jboss-web.xml");
                    Element jbossWeb = jbossWebDoc.getDocumentElement();
                    metaData.importXml(jbossWeb);
                }
            } catch (Exception e) {
                throw new DeploymentException("Failed to parse WEB-INF/jboss-web.xml", e);
            }
        } catch (DeploymentException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Failed to parse descriptors for war(" + warURL + ")", e);
        }
        String webContext = ctxPath;
        if (webContext == null) webContext = metaData.getContextRoot();
        if (webContext == null) {
            webContext = warName;
            webContext = webContext.replace('\\', '/');
            if (webContext.endsWith("/")) webContext = webContext.substring(0, webContext.length() - 1);
            int prefix = webContext.lastIndexOf('/');
            if (prefix > 0) webContext = webContext.substring(prefix + 1);
            int suffix = webContext.lastIndexOf(".");
            if (suffix > 0) webContext = webContext.substring(0, suffix);
            int index = 0;
            for (; index < webContext.length(); index++) {
                char c = webContext.charAt(index);
                if (Character.isDigit(c) == false && c != '.') break;
            }
            webContext = webContext.substring(index);
        }
        if (webContext.length() > 0 && webContext.charAt(0) != '/') webContext = "/" + webContext; else if (webContext.equals("/")) webContext = "";
        metaData.setContextRoot(webContext);
    }
