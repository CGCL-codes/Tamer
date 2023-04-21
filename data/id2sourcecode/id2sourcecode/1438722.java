    public static void preview(HashMap glossary, HttpServletResponse res) throws ServletException, IOException {
        String templateId = "";
        String pageId = "";
        try {
            Class c = Class.forName(CDSServlet.templateLoaderClass);
            templateLoader = (TemplateLoader) c.newInstance();
            templateLoader.setTemplateRoot(CDSServlet.templatePath);
            templateLoader.setDefaultIndex(CDSServlet.listTemplate);
            templateLoader.setDefaultObject(CDSServlet.fileTemplate);
        } catch (Exception e) {
            System.err.println("Error loading template loader class:");
            e.printStackTrace(System.err);
            boolean errorInitializing = true;
        }
        CofaxPage page = new CofaxPage();
        page.reset();
        PrintWriter out = res.getWriter();
        try {
            pageId = glossary.get("request:pageId").toString();
            templateLoader.setTemplateSearch(CDSServlet.templatePath + "/" + glossary.get("request:templateSearch").toString());
            templateId = templateLoader.choose(glossary.get("request:FILENAME").toString() + ".htm", "", ".htm");
            if (templateId.equals("")) {
                page.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                page.setErrorMsg("Error: Template not found to satisfy request.");
            }
            modifyTemplateToPreview(templateId);
            String suffix = templateId.substring(templateId.lastIndexOf(".") + 1);
            String templatePreview = "articlePreview" + intRdm + "." + suffix;
            String path = templateId.substring(0, templateId.lastIndexOf("/")) + "/" + templatePreview;
            templatesToDelete.add(path);
            templateString = replaceTagsInTemplate(templateString);
            File deletefile = new File(path);
            boolean delete = true;
            if (deletefile.exists()) {
                delete = deletefile.delete();
            }
            if (delete) {
                File newTemplate = new File(path);
                newTemplate.createNewFile();
                PrintWriter fileOutputStream = new PrintWriter(new FileWriter(path, true));
                fileOutputStream.println(templateString);
                fileOutputStream.close();
            } else {
                System.err.println("CofaxToolsPreview : Can not delete " + templatePreview);
            }
            String urlString = glossary.get("request:requestedUrl") + "?template=" + templatePreview + "&ITEMID=" + glossary.get("request:ITEMID");
            URL urlcnx = new URL(urlString);
            InputStream is = urlcnx.openStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String line;
            PrintWriter status = res.getWriter();
            while ((line = bf.readLine()) != null) {
                status.print(line);
            }
            status.close();
            while (templatesToDelete.size() > 0) {
                try {
                    File deletefile2 = new File((String) templatesToDelete.get(0));
                    if (deletefile2.exists()) {
                        delete = deletefile2.delete();
                    }
                } catch (Exception e) {
                    System.err.println("CofaxToolsPreview : Exception:" + e);
                }
                templatesToDelete.remove(0);
            }
        } catch (Exception e) {
            System.err.println("CofaxToolsPreview : Exception:" + e);
        }
    }
