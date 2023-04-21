    public void exportNotebook(int exportType, String dstFileName) {
        logger.debug("=-> notebook export: " + dstFileName);
        if (MindRaider.notebookCustodian.activeNotebookResource != null) {
            NotebookResourceExpanded notebookResourceExpanded;
            String resourcesFilePrefix = MindRaider.installationDirectory + File.separator + "java" + File.separator + "src" + File.separator + "resources" + File.separator;
            String xslFilePrefix = resourcesFilePrefix + "xsl" + File.separator;
            String cssFilePrefix = resourcesFilePrefix + "css" + File.separator;
            String jsFilePrefix = resourcesFilePrefix + "js" + File.separator;
            try {
                switch(exportType) {
                    case FORMAT_TWIKI:
                        notebookResourceExpanded = new NotebookResourceExpanded(MindRaider.notebookCustodian.activeNotebookResource, TWikifier.getInstance());
                        notebookResourceExpanded.save(dstFileName, xslFilePrefix + "export2TWiki.xsl");
                        break;
                    case FORMAT_TWIKI_HTML:
                        notebookResourceExpanded = new NotebookResourceExpanded(MindRaider.notebookCustodian.activeNotebookResource, TWikifier.getInstance());
                        String tmpOpml = dstFileName + ".tmp";
                        notebookResourceExpanded.save(tmpOpml, xslFilePrefix + "export2TWiki.xsl");
                        FileInputStream fileInputStream = new FileInputStream(new File(tmpOpml));
                        String htmlContent = "<html>" + " <head>" + "   <style type='text/css'>" + "     ul, ol {" + "         margin-top: 0px;" + "         margin-bottom: 3px;" + "         margin-left: 25px;" + "     }" + "     body {" + "         font-family: arial, helvetica, sans-serif; " + "         font-size: small;" + "     }" + "   </style>" + " </head>" + "<body>\n" + TwikiToHtml.translate(fileInputStream) + "\n</body>" + "</html>";
                        File twikiHtmlFile = new File(dstFileName);
                        FileWriter fileWriter = null;
                        try {
                            fileWriter = new FileWriter(twikiHtmlFile);
                            fileWriter.write(htmlContent);
                        } finally {
                            fileWriter.flush();
                            fileWriter.close();
                        }
                        break;
                    case FORMAT_OPML:
                        notebookResourceExpanded = new NotebookResourceExpanded(MindRaider.notebookCustodian.activeNotebookResource, Opmlizer.getInstance());
                        notebookResourceExpanded.save(dstFileName, xslFilePrefix + "export2Opml.xsl");
                        break;
                    case FORMAT_OPML_HTML:
                        notebookResourceExpanded = new NotebookResourceExpanded(MindRaider.notebookCustodian.activeNotebookResource, Opmlizer.getInstance());
                        tmpOpml = dstFileName + ".tmp";
                        notebookResourceExpanded.save(tmpOpml, xslFilePrefix + "export2OpmlInternal.xsl");
                        Xsl.xsl(tmpOpml, dstFileName, xslFilePrefix + "opml2Html.xsl");
                        File dstDir = new File(dstFileName);
                        String dstDirectory = dstDir.getParent();
                        String srcOpmlCss = cssFilePrefix + "opml.css";
                        String destOpmlCss = dstDirectory + File.separator + "opml.css";
                        FileUtils.copyFile(new File(srcOpmlCss), new File(destOpmlCss));
                        String srcOpmlJs = jsFilePrefix + "opml.js";
                        String destOpmlJs = dstDirectory + File.separator + "opml.js";
                        FileUtils.copyFile(new File(srcOpmlJs), new File(destOpmlJs));
                        break;
                }
            } catch (Exception e) {
                logger.error("Unable to export notebook!", e);
            }
        }
    }
