    public Element nextOutFile() {
        htmlDoc = new XhtmlDocument(getOutFileName(++nOutFileIndex, false), nType);
        htmlDoc.setConfig(config);
        if (template != null) {
            htmlDoc.readFromTemplate(template);
        } else if (bNeedHeaderFooter) {
            htmlDoc.createHeaderFooter();
        }
        outFiles.add(nOutFileIndex, htmlDoc);
        converterResult.addDocument(htmlDoc);
        htmlDOM = htmlDoc.getContentDOM();
        Element rootElement = htmlDOM.getDocumentElement();
        styleCv.applyDefaultLanguage(rootElement);
        rootElement.insertBefore(htmlDOM.createComment("This file was converted to xhtml by " + (ofr.isText() ? "Writer" : (ofr.isSpreadsheet() ? "Calc" : "Impress")) + "2xhtml ver. " + ConverterFactory.getVersion() + ". See http://writer2latex.sourceforge.net for more info."), rootElement.getFirstChild());
        if (!ofr.isPresentation()) {
            StyleInfo pageInfo = new StyleInfo();
            styleCv.getPageSc().applyDefaultWritingDirection(pageInfo);
            styleCv.getPageSc().applyStyle(pageInfo, htmlDoc.getContentNode());
        }
        Element title = htmlDoc.getTitleNode();
        if (title != null) {
            String sTitle = metaData.getTitle();
            if (sTitle == null) {
                sTitle = htmlDoc.getFileName();
            }
            title.appendChild(htmlDOM.createTextNode(sTitle));
        }
        Element head = htmlDoc.getHeadNode();
        if (head != null) {
            if (nType == XhtmlDocument.XHTML10) {
                Element meta = htmlDOM.createElement("meta");
                meta.setAttribute("http-equiv", "Content-Type");
                meta.setAttribute("content", "text/html; charset=" + htmlDoc.getEncoding().toLowerCase());
                head.appendChild(meta);
            } else if (nType == XhtmlDocument.HTML5) {
                Element meta = htmlDOM.createElement("meta");
                meta.setAttribute("charset", htmlDoc.getEncoding().toUpperCase());
                head.appendChild(meta);
            }
            if (!bOPS) {
                createMeta(head, "description", metaData.getDescription());
                createMeta(head, "keywords", metaData.getKeywords());
                if (config.xhtmlUseDublinCore()) {
                    head.setAttribute("profile", "http://dublincore.org/documents/2008/08/04/dc-html/");
                    Element dclink = htmlDOM.createElement("link");
                    dclink.setAttribute("rel", "schema.DC");
                    dclink.setAttribute("href", "http://purl.org/dc/elements/1.1/");
                    head.appendChild(dclink);
                    createMeta(head, "DC.title", metaData.getTitle());
                    String sDCSubject = "";
                    if (metaData.getSubject() != null && metaData.getSubject().length() > 0) {
                        sDCSubject = metaData.getSubject();
                    }
                    if (metaData.getKeywords() != null && metaData.getKeywords().length() > 0) {
                        if (sDCSubject.length() > 0) {
                            sDCSubject += ", ";
                        }
                        sDCSubject += metaData.getKeywords();
                    }
                    createMeta(head, "DC.subject", sDCSubject);
                    createMeta(head, "DC.description", metaData.getDescription());
                    createMeta(head, "DC.creator", metaData.getCreator());
                    createMeta(head, "DC.date", metaData.getDate());
                    createMeta(head, "DC.language", metaData.getLanguage());
                }
            }
            if (!bOPS && config.xhtmlCustomStylesheet().length() > 0) {
                Element htmlStyle = htmlDOM.createElement("link");
                htmlStyle.setAttribute("rel", "stylesheet");
                htmlStyle.setAttribute("type", "text/css");
                htmlStyle.setAttribute("media", "all");
                htmlStyle.setAttribute("href", config.xhtmlCustomStylesheet());
                head.appendChild(htmlStyle);
            }
            if (!bOPS && config.separateStylesheet() && config.xhtmlFormatting() > XhtmlConfig.IGNORE_STYLES) {
                Element htmlStyle = htmlDOM.createElement("link");
                htmlStyle.setAttribute("rel", "stylesheet");
                htmlStyle.setAttribute("type", "text/css");
                htmlStyle.setAttribute("media", "all");
                htmlStyle.setAttribute("href", sTargetFileName + "-styles.css");
                head.appendChild(htmlStyle);
            }
            if (bOPS && styleSheet != null) {
                Element sty = htmlDOM.createElement("link");
                sty.setAttribute("rel", "stylesheet");
                sty.setAttribute("type", "text/css");
                sty.setAttribute("href", EPUB_CUSTOM_STYLESHEET);
                head.appendChild(sty);
            }
            if (isOPS() && config.xhtmlFormatting() > XhtmlConfig.IGNORE_STYLES) {
                Element htmlStyle = htmlDOM.createElement("link");
                htmlStyle.setAttribute("rel", "stylesheet");
                htmlStyle.setAttribute("type", "text/css");
                htmlStyle.setAttribute("href", EPUB_STYLESHEET);
                head.appendChild(htmlStyle);
            }
        }
        if (!textCv.sections.isEmpty()) {
            Iterator<Node> iter = textCv.sections.iterator();
            while (iter.hasNext()) {
                Element section = (Element) iter.next();
                String sStyleName = Misc.getAttribute(section, XMLString.TEXT_STYLE_NAME);
                Element div = htmlDOM.createElement("div");
                htmlDoc.getContentNode().appendChild(div);
                htmlDoc.setContentNode(div);
                StyleInfo sectionInfo = new StyleInfo();
                styleCv.getSectionSc().applyStyle(sStyleName, sectionInfo);
                styleCv.getSectionSc().applyStyle(sectionInfo, div);
            }
        }
        return htmlDoc.getContentNode();
    }
