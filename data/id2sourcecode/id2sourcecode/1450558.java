    private void processImage(String entryName, String imageTitle) throws IOException, XMLStreamException {
        int height = this.iiurlheight;
        if (height == -1 && this.iiurlwidth != -1) {
            height = this.iiurlwidth;
        }
        LOG.info(entryName + " " + imageTitle + " width:" + iiurlwidth);
        boolean found = false;
        String url = this.base_api + "?action=query" + "&prop=imageinfo" + (iiurlwidth != -1 ? "&iiurlwidth=" + iiurlwidth : "") + (height != -1 ? "&iiurlheight=" + height : "") + "&format=xml" + "&titles=" + escape(imageTitle) + "&iiprop=timestamp|user|comment|url|size|dimensions|mime|archivename|bitdepth";
        LOG.info(url);
        XMLEventReader reader = this.xmlInputFactory.createXMLEventReader(openStream(url));
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement e = event.asStartElement();
                if (e.getName().getLocalPart().equals("ii")) {
                    found = true;
                    switch(this.output_type) {
                        case text:
                            {
                                System.out.print(entryName + "\t");
                                System.out.print(imageTitle + "\t");
                                System.out.print(attr(e, "timestamp") + "\t");
                                System.out.print(attr(e, "user") + "\t");
                                System.out.print(attr(e, "size") + "\t");
                                System.out.print(attr(e, "width") + "\t");
                                System.out.print(attr(e, "height") + "\t");
                                System.out.print(attr(e, "url") + "\t");
                                System.out.print(attr(e, "thumburl") + "\t");
                                System.out.print(attr(e, "descriptionurl"));
                                System.out.println();
                                break;
                            }
                        case xhtml:
                            {
                                System.out.println("<span>");
                                System.out.println("<a href=\"" + attr(e, "descriptionurl") + "\">");
                                System.out.println("<img src=\"" + attr(e, "thumburl") + "\" " + " width=\"" + attr(e, "thumbwidth") + "\" " + " height=\"" + attr(e, "thumbheight") + "\" " + " alt=\"" + XMLUtilities.escape(imageTitle) + "\" " + "/>");
                                System.out.println("</a>");
                                System.out.println("</span>");
                                break;
                            }
                        case wiki:
                            {
                                System.out.println("|" + imageTitle.replace(' ', '_') + "|");
                                break;
                            }
                    }
                }
            }
        }
        reader.close();
        if (!found) {
            LOG.info("ImageInfo not found for " + imageTitle + " " + url);
        }
    }
