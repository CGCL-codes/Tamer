    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("a");
        String id = req.getParameter("id");
        String url = config.getString(MultiPatternContentHandler.KEY_PREFIX + id + ".url");
        String encoding = config.getString("encoding", "UTF-8");
        req.setCharacterEncoding(encoding);
        res.setCharacterEncoding(encoding);
        PrintWriter out = res.getWriter();
        if (action == null) {
            action = "list";
        }
        if (propertiesFile == null || config == null) {
            printSimpleMsg(out, "No Configuration", "Configuration is not completed, or problem occurs, please check server log.");
            return;
        }
        if (action.equals("get")) {
            if (id == null) {
                res.setContentType("text/html; charset=" + encoding);
                printSimpleMsg(out, "Parameter Missing", "id has not been set!");
                return;
            }
            if (!siteSet.contains(id)) {
                res.setContentType("text/html; charset=" + encoding);
                printSimpleMsg(out, "No configuration", "No configuration for specified ID!");
                return;
            }
            try {
                ContentProcessor processor = new ContentProcessor(config);
                processor.setLoader(loader);
                processor.setHandler(handler);
                handler.setHandler(id);
                processor.setContentUrl(new URL(url));
                processor.process();
                res.setContentType("text/xml; charset=" + encoding);
                RSS_1_0_Exporter exporter = new RSS_1_0_Exporter(out, encoding);
                exporter.write(processor.getChannel());
                return;
            } catch (ConfigurationException ce) {
                res.setContentType("text/html; charset=" + encoding);
                printSimpleMsg(out, "Configuration Error", "Error setup configuration: " + ce.getCause().getMessage());
                return;
            } catch (MalformedURLException e) {
                res.setContentType("text/html; charset=" + encoding);
                printSimpleMsg(out, "Invalid URL", "The input URL is invalid: " + url + ", details:" + e.getMessage());
                return;
            } catch (LoaderException e) {
                res.setContentType("text/html; charset=" + encoding);
                printSimpleMsg(out, "Error Loading URL", "Requested URL cannot be loaded: " + e.getCause().getMessage());
                return;
            } catch (ContentHandlerException e) {
                res.setContentType("text/html; charset=" + encoding);
                printSimpleMsg(out, "Error Parsing URL", "Requested URL cannot be parsed: " + e.getCause().getMessage());
                return;
            } catch (ContentProcessorException e) {
                res.setContentType("text/html; charset=" + encoding);
                printSimpleMsg(out, "Error Parsing URL", "An error has occured while converting content: " + e.getCause().getMessage());
                return;
            }
        } else if (action.equals("reset")) {
            try {
                setUp();
            } catch (ConfigurationException ce) {
                printSimpleMsg(out, "Configuration Error", "Error setup configuration: " + ce.getCause().getMessage());
            }
            return;
        } else if (action.equals("list")) {
            printSimpleMsg(out, "Avaliable Site", "TODO: List");
            return;
        }
    }
