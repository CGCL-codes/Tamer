    public static void loadCapabilities(Configuration config, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String level = "info.";
        if (config.isDebug()) level = "debug.";
        Log log = LogFactory.getLog(level + "fr.brgm.exows.GetCapabilities");
        String service = ConnectorServlet.getParameter(request, "service");
        String version = ConnectorServlet.getParameter(request, "version");
        String lang = ConnectorServlet.getParameter(request, "language");
        if (lang == null) {
            lang = "";
        }
        String configName = ConnectorServlet.getParameter(request, "config");
        if (configName == null) {
            configName = "default";
        }
        String context_path = request.getRequestURL().toString();
        if (!configName.equalsIgnoreCase("default")) context_path += configName + "/";
        context_path += "?";
        response.setCharacterEncoding("UTF8");
        PrintWriter sortie = response.getWriter();
        if (service != null) {
            String result = "";
            Server serv;
            String repository;
            List<LayerName> ll;
            String defaultLang;
            if (service.equalsIgnoreCase("WMS")) {
                if (version == null) version = config.getWMS().getDefaultVersion();
                serv = config.getWMS().getServerByLang(lang);
                repository = config.getWMS().getRepository();
                ll = config.getWMS().getLayerList();
                lang = config.getWMS().getLangOrDefaultLang(lang);
                defaultLang = config.getWMS().getDefaultLang();
            } else {
                if (version == null) version = config.getWFS().getDefaultVersion();
                serv = config.getWFS().getServerByLang(lang);
                repository = config.getWFS().getRepository();
                ll = config.getWFS().getLayerList();
                lang = config.getWFS().getLangOrDefaultLang(lang);
                defaultLang = config.getWFS().getDefaultLang();
            }
            lang = lang.toLowerCase();
            String version_normalisee = "";
            if (version != null) version_normalisee = version.replace(".", "_");
            String searchedFile = "getCapabilities-" + service.toLowerCase() + "-" + version_normalisee + "-" + lang + ".xml";
            File fileSearchedFile = new File(repository + File.separator + searchedFile);
            if (!fileSearchedFile.exists()) {
                searchedFile = "getCapabilities-" + service.toLowerCase() + "-" + version_normalisee + "-" + defaultLang + ".xml";
                fileSearchedFile = new File(repository + File.separator + searchedFile);
            }
            String servURL;
            if (fileSearchedFile.exists()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileSearchedFile)));
                String line;
                while ((line = br.readLine()) != null) {
                    result += line + ls;
                }
                if (serv != null) {
                    servURL = serv.getUrl();
                } else {
                    servURL = StringUtils.substringBetween(result, "xlink:href=\"", "\"");
                    if (StringUtils.contains(servURL, "?")) context_path += "?";
                }
                br.close();
            } else {
                log.trace("Unable to find the file in repository, request sent to server: " + fileSearchedFile.toString());
                servURL = serv.getUrl();
                String separator = "?";
                if (StringUtils.contains(servURL, "?")) separator = "&";
                URL url;
                if (version != null) url = new URL(servURL + separator + "request=GetCapabilities&version=" + version + "&service=" + service); else url = new URL(servURL + separator + "request=GetCapabilities&service=" + service);
                URLConnection urlc = url.openConnection();
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                Document doc = builder.parse(urlc.getInputStream());
                if (version == null || version.equalsIgnoreCase("1.3.0")) {
                    try {
                        version = ((Element) doc.getElementsByTagName("WMS_Capabilities").item(0)).getAttribute("version");
                    } catch (NullPointerException npe) {
                        try {
                            version = ((Element) doc.getElementsByTagName("WMT_MS_Capabilities").item(0)).getAttribute("version");
                        } catch (NullPointerException npee) {
                        }
                    }
                }
                try {
                    Element GetCapabilitiesNode = (Element) doc.getElementsByTagName("GetCapabilities").item(0);
                    Element GetCapabilitiesGetNode = (Element) GetCapabilitiesNode.getElementsByTagName("Get").item(0);
                    servURL = ((Element) GetCapabilitiesGetNode.getElementsByTagName("OnlineResource").item(0)).getAttribute("xlink:href");
                } catch (NullPointerException npe) {
                }
                TransformerFactory transfac = TransformerFactory.newInstance();
                Transformer trans = transfac.newTransformer();
                trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                trans.setOutputProperty(OutputKeys.INDENT, "yes");
                trans.setOutputProperty(OutputKeys.ENCODING, "UTF8");
                StringWriter sw = new StringWriter();
                StreamResult result2 = new StreamResult(sw);
                DOMSource source = new DOMSource(modifyGetCapabilities(service, version, doc, config, configName, serv, lang, log, ll));
                trans.transform(source, result2);
                result = sw.toString();
            }
            if (!fileSearchedFile.exists() && config.getMode().equals(ConnectorServlet.EXOWS_MODE_NORMAL)) {
                servURL = StringUtils.replace(servURL, "&", "&amp;");
                result = StringUtils.replace(result, servURL, context_path);
            }
            response.setContentType("text/xml; charset=utf-8");
            sortie.write(result);
        } else {
            sortie.println("eXows");
            sortie.println("---------------");
            sortie.println("REQUEST: getCapabilities");
            sortie.println("SERVICE is empty, the request cannot be completed.");
            log.trace("SERVICE is empty, the request cannot be completed.");
        }
        sortie.close();
    }
