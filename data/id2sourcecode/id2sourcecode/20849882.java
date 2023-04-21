    private static final Templates getTemplate(String xsltName) {
        Templates translet = null;
        synchronized (MAP_TEMPLATES) {
            translet = MAP_TEMPLATES.get(xsltName);
            if (translet == null) {
                TransformerFactory tFactory = TransformerFactory.newInstance();
                tFactory.setAttribute("translet-name", xsltName.substring(0, 1).toUpperCase() + xsltName.substring(1) + "Xslt");
                tFactory.setAttribute("destination-directory", ContextKeeper.getInstallPath() + "WEB-INF/classes/");
                tFactory.setAttribute("package-name", "org.jeromedl.xslt");
                tFactory.setAttribute("generate-translet", Boolean.TRUE);
                tFactory.setAttribute("auto-translet", Boolean.TRUE);
                tFactory.setAttribute("use-classpath", Boolean.TRUE);
                logger.finer("[DEBUG] name: " + tFactory.getAttribute("translet-name"));
                try {
                    Source source = null;
                    if (xsltName.startsWith("http")) {
                        URL url = new URL(xsltName);
                        source = new StreamSource(url.openStream());
                    } else {
                        String sName = ContextKeeper.getInstallPath() + "xsl/" + xsltName + Statics.XSL;
                        logger.finer("[DEBUG] sName " + sName);
                        source = new StreamSource(new File(sName));
                    }
                    translet = tFactory.newTemplates(source);
                } catch (TransformerConfigurationException tcex) {
                    logger.log(Level.INFO, "Error while getting templates", tcex);
                } catch (MalformedURLException muex) {
                    logger.log(Level.INFO, "Error while getting templates", muex);
                } catch (IOException ioex) {
                    logger.log(Level.INFO, "Error while getting templates", ioex);
                } finally {
                    MAP_TEMPLATES.put(xsltName, translet);
                }
            }
        }
        return translet;
    }
