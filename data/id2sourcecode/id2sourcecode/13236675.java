    private static final Templates getTemplate(String xsltName) {
        Templates translet = null;
        synchronized (MAP_TEMPLATES) {
            translet = (Templates) MAP_TEMPLATES.get(xsltName);
            if (translet == null) {
                TransformerFactory tFactory = TransformerFactory.newInstance();
                tFactory.setAttribute("translet-name", xsltName.substring(0, 1).toUpperCase() + xsltName.substring(1) + "Xslt");
                tFactory.setAttribute("destination-directory", ContextKeeper.getInstallDir() + "WEB-INF/classes/");
                tFactory.setAttribute("package-name", "com.skruk.elvis.xslt");
                tFactory.setAttribute("generate-translet", Boolean.TRUE);
                tFactory.setAttribute("auto-translet", Boolean.TRUE);
                tFactory.setAttribute("use-classpath", Boolean.TRUE);
                System.out.println("[DEBUG] name: " + tFactory.getAttribute("translet-name"));
                try {
                    Source source = null;
                    if (xsltName.startsWith("http")) {
                        URL url = new URL(xsltName);
                        source = new StreamSource(url.openStream());
                    } else {
                        String sName = ContextKeeper.getInstallDir() + "xsl/" + xsltName + ".xsl";
                        System.out.println("[DEBUG] sName " + sName);
                        source = new StreamSource(new File(sName));
                    }
                    translet = tFactory.newTemplates(source);
                } catch (TransformerConfigurationException tcex) {
                    tcex.printStackTrace();
                } catch (MalformedURLException muex) {
                    muex.printStackTrace();
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                } finally {
                    MAP_TEMPLATES.put(xsltName, translet);
                }
            }
        }
        return translet;
    }
