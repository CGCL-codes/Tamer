    private static Document generateDocument(RSS rssContent) {
        Document doc = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElement("rss");
            root.setAttribute("version", rssContent.getVersion());
            Iterator channels = rssContent.getChannels().iterator();
            while (channels.hasNext()) {
                Channel channel = (Channel) channels.next();
                Element chan = doc.createElement("channel");
                if (channel.getTitle() != null) {
                    Text content = doc.createTextNode(channel.getTitle());
                    Element elem = doc.createElement("title");
                    elem.appendChild(content);
                    chan.appendChild(elem);
                }
                if (channel.getLink() != null) {
                    Text content = doc.createTextNode(channel.getLink());
                    Element elem = doc.createElement("link");
                    elem.appendChild(content);
                    chan.appendChild(elem);
                }
                if (channel.getDescription() != null) {
                    Text content = doc.createTextNode(channel.getDescription());
                    Element elem = doc.createElement("description");
                    elem.appendChild(content);
                    chan.appendChild(elem);
                }
                if (channel.getLanguage() != null) {
                    Text content = doc.createTextNode(channel.getLanguage());
                    Element elem = doc.createElement("language");
                    elem.appendChild(content);
                    chan.appendChild(elem);
                }
                if (channel.getCopyright() != null) {
                    Text content = doc.createTextNode(channel.getCopyright());
                    Element elem = doc.createElement("copyright");
                    elem.appendChild(content);
                    chan.appendChild(elem);
                }
                if (channel.getWebMaster() != null) {
                    Text content = doc.createTextNode(channel.getWebMaster());
                    Element elem = doc.createElement("webMaster");
                    elem.appendChild(content);
                    chan.appendChild(elem);
                }
                if (channel.getPubDate() != null) {
                    Text content = doc.createTextNode(sdf.format(channel.getPubDate()));
                    Element elem = doc.createElement("pubDate");
                    elem.appendChild(content);
                    chan.appendChild(elem);
                }
                if (channel.getLastBuildDate() != null) {
                    Text content = doc.createTextNode(sdf.format(channel.getLastBuildDate()));
                    Element elem = doc.createElement("lastBuildDate");
                    elem.appendChild(content);
                    chan.appendChild(elem);
                }
                Iterator categories = channel.getCategories().iterator();
                while (categories.hasNext()) {
                    Channel.Category category = (Channel.Category) categories.next();
                    Text content = doc.createTextNode(category.getName());
                    Element elem = doc.createElement("category");
                    if (category.getDomain() != null) {
                        elem.setAttribute("domain", category.getDomain());
                    }
                    elem.appendChild(content);
                    chan.appendChild(elem);
                }
                if (channel.getGenerator() != null) {
                    Text content = doc.createTextNode(channel.getGenerator());
                    Element elem = doc.createElement("generator");
                    elem.appendChild(content);
                    chan.appendChild(elem);
                }
                if (channel.getDocs() != null) {
                    Text content = doc.createTextNode(channel.getDocs());
                    Element elem = doc.createElement("docs");
                    elem.appendChild(content);
                    chan.appendChild(elem);
                }
                if (channel.getCloud() != null) {
                    Channel.Cloud cloud = channel.getCloud();
                    Element elem = doc.createElement("cloud");
                    elem.setAttribute("domain", cloud.getDomain());
                    elem.setAttribute("port", String.valueOf(cloud.getPort()));
                    elem.setAttribute("path", cloud.getPath());
                    elem.setAttribute("registerProcedure", cloud.getRegisterProcedure());
                    elem.setAttribute("protocol", cloud.getProtocol());
                    chan.appendChild(elem);
                }
                if (channel.getTtl() >= 0) {
                    Text content = doc.createTextNode(String.valueOf(channel.getTtl()));
                    Element elem = doc.createElement("ttl");
                    elem.appendChild(content);
                    chan.appendChild(elem);
                }
                if (channel.getImage() != null) {
                    Channel.Image image = channel.getImage();
                    Element elem = doc.createElement("image");
                    Element url = doc.createElement("url");
                    url.appendChild(doc.createTextNode(image.getUrl()));
                    elem.appendChild(url);
                    Element title = doc.createElement("title");
                    title.appendChild(doc.createTextNode(image.getTitle()));
                    elem.appendChild(title);
                    Element link = doc.createElement("link");
                    link.appendChild(doc.createTextNode(image.getLink()));
                    elem.appendChild(link);
                    if (image.getWidth() > 0) {
                        Text wct = doc.createTextNode(String.valueOf(image.getWidth()));
                        Element w = doc.createElement("width");
                        w.appendChild(wct);
                        elem.appendChild(w);
                    }
                    if (image.getHeight() > 0) {
                        Text hct = doc.createTextNode(String.valueOf(image.getHeight()));
                        Element h = doc.createElement("height");
                        h.appendChild(hct);
                        elem.appendChild(h);
                    }
                    if (image.getDescription() != null) {
                        Text descct = doc.createTextNode(image.getDescription());
                        Element desc = doc.createElement("description");
                        desc.appendChild(descct);
                        elem.appendChild(desc);
                    }
                    chan.appendChild(elem);
                }
                if (channel.getRating() != null) {
                    Text content = doc.createTextNode(channel.getRating());
                    Element elem = doc.createElement("rating");
                    elem.appendChild(content);
                    chan.appendChild(elem);
                }
                if (channel.getTextInput() != null) {
                    Channel.TextInput tinput = channel.getTextInput();
                    Element elem = doc.createElement("textInput");
                    Text content = doc.createTextNode(tinput.getTitle());
                    Element subelem = doc.createElement("title");
                    subelem.appendChild(content);
                    elem.appendChild(subelem);
                    content = doc.createTextNode(tinput.getDescription());
                    subelem = doc.createElement("description");
                    subelem.appendChild(content);
                    elem.appendChild(subelem);
                    content = doc.createTextNode(tinput.getName());
                    subelem = doc.createElement("name");
                    subelem.appendChild(content);
                    elem.appendChild(subelem);
                    content = doc.createTextNode(tinput.getLink());
                    subelem = doc.createElement("link");
                    subelem.appendChild(content);
                    elem.appendChild(subelem);
                    chan.appendChild(elem);
                }
                if (channel.getSkipHours() != null) {
                    Element skip = doc.createElement("skipHours");
                    Element hour;
                    Text hourct;
                    StringTokenizer hours = new StringTokenizer(channel.getSkipHours(), ",");
                    while (hours.hasMoreTokens()) {
                        String hr = hours.nextToken().trim();
                        if (hr.indexOf("-") != -1) {
                            int x = Integer.parseInt(hr.substring(0, hr.indexOf("-")));
                            int y = Integer.parseInt(hr.substring(hr.indexOf("-") + 1));
                            while (true) {
                                hour = doc.createElement("hour");
                                hourct = doc.createTextNode(String.valueOf(x % 24));
                                hour.appendChild(hourct);
                                skip.appendChild(hour);
                                if (x % 24 == y) break; else x++;
                            }
                        } else {
                            if (!"".equals(hr)) {
                                hour = doc.createElement("hour");
                                hourct = doc.createTextNode(hr);
                                hour.appendChild(hourct);
                                skip.appendChild(hour);
                            }
                        }
                    }
                    chan.appendChild(skip);
                }
                if (channel.getSkipDays() != (byte) 0 && channel.getSkipDays() != (byte) 0x80) {
                    Element skip = doc.createElement("skipDays");
                    Element day;
                    Text dayct;
                    byte days = channel.getSkipDays();
                    days = (byte) ((days << 25) >>> 25);
                    if ((days & Channel.SKIP_SUNDAY) == Channel.SKIP_SUNDAY) {
                        day = doc.createElement("day");
                        dayct = doc.createTextNode("Sunday");
                        day.appendChild(dayct);
                        skip.appendChild(day);
                    }
                    if ((days & Channel.SKIP_MONDAY) == Channel.SKIP_MONDAY) {
                        day = doc.createElement("day");
                        dayct = doc.createTextNode("Monday");
                        day.appendChild(dayct);
                        skip.appendChild(day);
                    }
                    if ((days & Channel.SKIP_TUESDAY) == Channel.SKIP_TUESDAY) {
                        day = doc.createElement("day");
                        dayct = doc.createTextNode("Tuesday");
                        day.appendChild(dayct);
                        skip.appendChild(day);
                    }
                    if ((days & Channel.SKIP_WEDNESDAY) == Channel.SKIP_WEDNESDAY) {
                        day = doc.createElement("day");
                        dayct = doc.createTextNode("Wednesday");
                        day.appendChild(dayct);
                        skip.appendChild(day);
                    }
                    if ((days & Channel.SKIP_THURSDAY) == Channel.SKIP_THURSDAY) {
                        day = doc.createElement("day");
                        dayct = doc.createTextNode("Thursday");
                        day.appendChild(dayct);
                        skip.appendChild(day);
                    }
                    if ((days & Channel.SKIP_FRIDAY) == Channel.SKIP_FRIDAY) {
                        day = doc.createElement("day");
                        dayct = doc.createTextNode("Friday");
                        day.appendChild(dayct);
                        skip.appendChild(day);
                    }
                    if ((days & Channel.SKIP_SATURDAY) == Channel.SKIP_SATURDAY) {
                        day = doc.createElement("day");
                        dayct = doc.createTextNode("Saturday");
                        day.appendChild(dayct);
                        skip.appendChild(day);
                    }
                    chan.appendChild(skip);
                }
                Iterator items = channel.getItems().iterator();
                while (items.hasNext()) {
                    Item item = (Item) items.next();
                    Element itelem = doc.createElement("item");
                    if (item.getTitle() != null) {
                        Text content = doc.createTextNode(item.getTitle());
                        Element elem = doc.createElement("title");
                        elem.appendChild(content);
                        itelem.appendChild(elem);
                    }
                    if (item.getLink() != null) {
                        Text content = doc.createTextNode(item.getLink());
                        Element elem = doc.createElement("link");
                        elem.appendChild(content);
                        itelem.appendChild(elem);
                    }
                    if (item.getDescription() != null) {
                        Text content = doc.createTextNode(item.getDescription());
                        Element elem = doc.createElement("description");
                        elem.appendChild(content);
                        itelem.appendChild(elem);
                    }
                    if (item.getAuthor() != null) {
                        Text content = doc.createTextNode(item.getAuthor());
                        Element elem = doc.createElement("author");
                        elem.appendChild(content);
                        itelem.appendChild(elem);
                    }
                    Iterator icategories = item.getCategories().iterator();
                    while (icategories.hasNext()) {
                        Item.Category category = (Item.Category) icategories.next();
                        Text content = doc.createTextNode(category.getName());
                        Element elem = doc.createElement("category");
                        if (category.getDomain() != null) {
                            elem.setAttribute("domain", category.getDomain());
                        }
                        elem.appendChild(content);
                        itelem.appendChild(elem);
                    }
                    if (item.getComments() != null) {
                        Text content = doc.createTextNode(item.getComments());
                        Element elem = doc.createElement("comments");
                        elem.appendChild(content);
                        itelem.appendChild(elem);
                    }
                    if (item.getEnclosure() != null) {
                        Item.Enclosure enclosure = item.getEnclosure();
                        Element elem = doc.createElement("enclosure");
                        elem.setAttribute("url", enclosure.getUrl());
                        elem.setAttribute("length", String.valueOf(enclosure.getLength()));
                        elem.setAttribute("type", enclosure.getType());
                        itelem.appendChild(elem);
                    }
                    if (item.getGuid() != null) {
                        Item.Guid guid = item.getGuid();
                        Element elem = doc.createElement("guid");
                        if (!guid.isPermaLink()) {
                            elem.setAttribute("isPermaLink", "false");
                        }
                        Text content = doc.createTextNode(guid.getId());
                        elem.appendChild(content);
                        itelem.appendChild(elem);
                    }
                    if (item.getPubDate() != null) {
                        Text content = doc.createTextNode(sdf.format(item.getPubDate()));
                        Element elem = doc.createElement("pubDate");
                        elem.appendChild(content);
                        itelem.appendChild(elem);
                    }
                    if (item.getSource() != null) {
                        Item.Source source = item.getSource();
                        Element elem = doc.createElement("source");
                        elem.setAttribute("url", source.getUrl());
                        if (source.getValue() != null) {
                            Text content = doc.createTextNode(source.getValue());
                            elem.appendChild(content);
                        }
                        itelem.appendChild(elem);
                    }
                    chan.appendChild(itelem);
                }
                root.appendChild(chan);
            }
            doc.appendChild(root);
        } catch (ParserConfigurationException e) {
            logger.error(e);
        } catch (FactoryConfigurationError e) {
            logger.error(e);
        }
        return doc;
    }
