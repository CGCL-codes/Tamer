    @Override
    public void close() throws ReportException {
        int imageCounter = 0;
        int textCounter = 0;
        int tabCounter = 0;
        int popupCounter = 0;
        for (Map.Entry<String, LinkedHashMap<Reportable, String>> sectionEntry : sections.entrySet()) {
            String sectionName = sectionEntry.getKey();
            try {
                OutputStream stream = getIOProvider().createOutputStream(sectionName.replace(' ', '_').toLowerCase() + ".html", "text/html");
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, Charset.forName("UTF-8")));
                if (writer != null) {
                    writer.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");
                    writer.write("<html><head>");
                    writer.write("<title>" + getName() + "</title>");
                    writer.write("<link rel=\"stylesheet\" href=\"screen.css\" type=\"text/css\" media=\"screen, projection\">\n");
                    writer.write("<link rel=\"stylesheet\" href=\"print.css\" type=\"text/css\" media=\"print\">\n");
                    writer.write("<!--[if lt IE 8]>");
                    writer.write("<link rel=\"stylesheet\" href=\"ie.css\" type=\"text/css\" media=\"screen, projection\">\n");
                    writer.write("<![endif]-->\n");
                    writer.write("<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\" media=\"screen\">\n");
                    writer.write("<link rel=\"stylesheet\" href=\"slimbox.css\" type=\"text/css\" media=\"screen\"/>\n");
                    writer.write("<script src=\"mootools.js\" type=\"text/javascript\"></script>\n");
                    writer.write("<script src=\"slimbox.js\" type=\"text/javascript\"></script>\n");
                    writer.write("</head><body>");
                    writer.write("<div class=\"container\" id=\"container\">\n");
                    writer.write("<div class=\"span-24 last\" id=\"header\"><h2>" + getName() + "</h2></div>\n");
                    writer.write("<div class=\"span-24 last\" id=\"menu\">\n");
                    writer.write("<div id=\"navcontainer\"><ul id=\"navlist\">\n");
                    for (String otherSectionName : sections.keySet()) {
                        if (sectionName.equals(otherSectionName)) {
                            writer.write("<li id=\"active\"><a href=\"#\" id=\"current\">" + otherSectionName + "</a></li>\n");
                        } else {
                            writer.write("<li><a href=\"" + otherSectionName.replace(' ', '_').toLowerCase() + ".html\">" + otherSectionName + "</a></li>\n");
                        }
                    }
                    writer.write("</ul></div>\n");
                    writer.write("</div>\n");
                    int i = 0;
                    int n = sectionEntry.getValue().entrySet().size();
                    int columns = 2;
                    int rows = n / 2 + (n % 2 > 0 ? 1 : 0);
                    for (Map.Entry<Reportable, String> reportableEntry : sectionEntry.getValue().entrySet()) {
                        Reportable reportable = reportableEntry.getKey();
                        String reportableName = reportableEntry.getValue();
                        int row = i / 2;
                        int column = i % 2;
                        writer.write("<div class=\"span-12" + (i % 2 == 1 ? " last" : "") + "\">\n");
                        writer.write("<div class=\"box" + (column == 0 ? " left-box" : "") + (column == (columns - 1) ? " right-box" : "") + (row == 0 ? " top-box" : "") + (row == rows - 1 ? " bottom-box" : "") + "\">\n");
                        writer.write("<div class=\"title\">" + reportableName + "</div>");
                        writer.write("<div class=\"content\">");
                        if (reportable instanceof Readable) {
                            textCounter++;
                            Readable readable = (Readable) reportable;
                            OutputStream textStream = getIOProvider().createOutputStream("_text" + textCounter + ".html", "text/html");
                            BufferedWriter textWriter = new BufferedWriter(new OutputStreamWriter(textStream, Charset.forName("UTF-8")));
                            textWriter.write("<html><head><link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\" media=\"screen\"></head><body class=\"text\">\n");
                            textWriter.write(readable.isInTargetEncoding() ? readable.toString() : HtmlEncoder.encode(readable.toString()) + "\n");
                            textWriter.write("</body></html>");
                            textWriter.flush();
                            textStream.close();
                            writer.write("<iframe src=\"_text" + textCounter + ".html\" scrolling=\"auto\" frameborder=\"0\"></iframe>");
                        } else if (reportable instanceof Renderable) {
                            imageCounter++;
                            Renderable renderable = (Renderable) reportable;
                            renderable.prepareRendering();
                            int width = renderable.getRenderWidth(900);
                            int height = renderable.getRenderHeight(540);
                            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                            Graphics2D g2 = (Graphics2D) img.getGraphics();
                            renderable.render(g2, width, height);
                            renderable.finishRendering();
                            g2.dispose();
                            String imageName = "_image" + imageCounter + ".png";
                            try {
                                final OutputStream out = getIOProvider().createOutputStream(imageName, "image/png");
                                ImageIO.write(img, "png", out);
                                out.close();
                            } catch (IOException e) {
                                LogService.getRoot().log(Level.WARNING, "Failed to render image: " + e, e);
                            }
                            writer.write("<a href=\"" + imageName + "\" rel=\"lightbox\" title=\"" + sectionName + ": " + reportableName + "\">" + "<img src=\"" + imageName + "\"/></a>");
                        } else if (reportable instanceof Tableable) {
                            tabCounter++;
                            Tableable tableable = (Tableable) reportable;
                            OutputStream tabStream = getIOProvider().createOutputStream("_tab" + tabCounter + ".html", "text/html");
                            BufferedWriter tabWriter = new BufferedWriter(new OutputStreamWriter(tabStream, Charset.forName("UTF-8")));
                            tabWriter.write("<html><head><link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\" media=\"screen\"></head><body class=\"tab\">\n");
                            tabWriter.write("<table>\n");
                            tabWriter.write("<tr>");
                            for (int c = 0; c < tableable.getColumnNumber(); c++) {
                                tabWriter.write("<td class=\"table_header\" width=\"" + Tools.formatNumber(1d / tableable.getColumnNumber(), 2) + "\">" + tableable.getColumnName(c) + "</td>");
                            }
                            tabWriter.write("</tr>\n");
                            for (int r = 0; r < tableable.getRowNumber(); r++) {
                                tabWriter.write("<tr>");
                                for (int c = 0; c < tableable.getColumnNumber(); c++) {
                                    String value = tableable.getCell(r, c);
                                    try {
                                        Double.valueOf(value.replace("%", ""));
                                        tabWriter.write("<td class=\"table_entry\" style=\"text-align:right\">" + HtmlEncoder.encode(value) + "</td>");
                                    } catch (NumberFormatException e) {
                                        if (maxTableCellContentSize >= 0) {
                                            if (value.length() > maxTableCellContentSize) {
                                                popupCounter++;
                                                tabWriter.write("<script language=\"javascript\" type=\"text/javascript\"> \n");
                                                tabWriter.write("<!-- \n");
                                                tabWriter.write("function popitup" + popupCounter + "() {");
                                                tabWriter.write("newwindow=window.open('','name','height=300,width=600');");
                                                tabWriter.write("var tmp = newwindow.document;");
                                                tabWriter.write("tmp.write('<html><head><title>Information</title>');");
                                                tabWriter.write("tmp.write('</head><body>');");
                                                tabWriter.write("tmp.write('<p>" + (encodeHTML ? HtmlEncoder.encode(value) : value) + "</p>');");
                                                tabWriter.write("tmp.write('</body></html>');");
                                                tabWriter.write("tmp.close();");
                                                tabWriter.write("}\n");
                                                tabWriter.write(" // -->\n");
                                                tabWriter.write("</script>\n");
                                                String linkValue = value.substring(0, maxTableCellContentSize);
                                                tabWriter.write("<td class=\"table_entry\">" + (encodeHTML ? HtmlEncoder.encode(linkValue) : linkValue) + "<a href=\"javascript:popitup" + popupCounter + "()\"> " + moreLinkText + "</a></td>");
                                            } else {
                                                tabWriter.write("<td class=\"table_entry\">" + (encodeHTML ? HtmlEncoder.encode(value) : value) + "</td>");
                                            }
                                        } else {
                                            tabWriter.write("<td class=\"table_entry\">" + (encodeHTML ? HtmlEncoder.encode(value) : value) + "</td>");
                                        }
                                    }
                                }
                                tabWriter.write("</tr>\n");
                            }
                            tabWriter.write("</table>\n");
                            tabWriter.write("</body></html>");
                            tabWriter.flush();
                            tabStream.close();
                            writer.write("<iframe src=\"_tab" + tabCounter + ".html\" scrolling=\"auto\" frameborder=\"0\"></iframe>");
                        }
                        writer.write("</div>\n");
                        writer.write("</div>\n");
                        writer.write("</div>\n");
                        i++;
                    }
                    writer.write("<div class=\"span-24 last\" id=\"footer\">RapidMiner Reporting, &#169; <a href=\"http://rapid-i.com\" targer=\"_blank\">Rapid-I GmbH 2010</a></div>\n");
                    writer.write("</div></body></html>");
                    writer.flush();
                    stream.close();
                }
            } catch (IOException e) {
                this.loggingHandler.logError("Error during creation of portal content: " + e);
            }
        }
        if (logoStream != null) {
            try {
                RenderedImage logoImage = ImageIO.read(logoStream);
                final OutputStream out = getIOProvider().createOutputStream("logo.png", "image/png");
                ImageIO.write(logoImage, "png", out);
                out.close();
            } catch (IOException e) {
                this.loggingHandler.logError("Problem during copying logo image: " + e);
            }
        }
        HashMap<String, String> replacements = new HashMap<String, String>();
        replacements.put("%{major_color}", majorColor);
        replacements.put("%{minor_color}", minorColor);
        replacements.put("%{background_color}", backgroundColor);
        replacements.put("%{box_color}", boxColor);
        try {
            copyTemplate("templates/slimbox.js", getIOProvider().createOutputStream("slimbox.js", "text/javascript"));
            copyTemplate("templates/mootools.js", getIOProvider().createOutputStream("mootools.js", "text/javascript"));
            copyTemplate("templates/ie.css", getIOProvider().createOutputStream("ie.css", "text/css"));
            copyTemplate("templates/print.css", getIOProvider().createOutputStream("print.css", "text/css"));
            copyTemplate("templates/screen.css", getIOProvider().createOutputStream("screen.css", "text/css"));
            copyTemplate("templates/slimbox.css", getIOProvider().createOutputStream("slimbox.css", "text/css"));
            fillTemplate("templates/style.css", getIOProvider().createOutputStream("style.css", "text/css"), replacements);
            copyImage("templates/closelabel.gif", getIOProvider(), "closelabel", "gif");
            copyImage("templates/nextlabel.gif", getIOProvider(), "nextlabel", "gif");
            copyImage("templates/prevlabel.gif", getIOProvider(), "prevlabel", "gif");
            copyImage("templates/loading.gif", getIOProvider(), "loading", "gif");
        } catch (IOException e) {
            this.loggingHandler.logError("Cannot copy resource: " + e);
        }
    }
