    public static void parse(URL url, PrintStream statusMsgStream, AppletViewerFactory factory) throws IOException {
        boolean isAppletTag = false;
        boolean isObjectTag = false;
        boolean isEmbedTag = false;
        String requiresNameWarning = amh.getMessage("parse.warning.requiresname");
        String paramOutsideWarning = amh.getMessage("parse.warning.paramoutside");
        String appletRequiresCodeWarning = amh.getMessage("parse.warning.applet.requirescode");
        String appletRequiresHeightWarning = amh.getMessage("parse.warning.applet.requiresheight");
        String appletRequiresWidthWarning = amh.getMessage("parse.warning.applet.requireswidth");
        String objectRequiresCodeWarning = amh.getMessage("parse.warning.object.requirescode");
        String objectRequiresHeightWarning = amh.getMessage("parse.warning.object.requiresheight");
        String objectRequiresWidthWarning = amh.getMessage("parse.warning.object.requireswidth");
        String embedRequiresCodeWarning = amh.getMessage("parse.warning.embed.requirescode");
        String embedRequiresHeightWarning = amh.getMessage("parse.warning.embed.requiresheight");
        String embedRequiresWidthWarning = amh.getMessage("parse.warning.embed.requireswidth");
        String appNotLongerSupportedWarning = amh.getMessage("parse.warning.appnotLongersupported");
        java.net.URLConnection conn = url.openConnection();
        Reader in = makeReader(conn.getInputStream());
        url = conn.getURL();
        int ydisp = 1;
        Hashtable atts = null;
        while (true) {
            c = in.read();
            if (c == -1) break;
            if (c == '<') {
                c = in.read();
                if (c == '/') {
                    c = in.read();
                    String nm = scanIdentifier(in);
                    if (nm.equalsIgnoreCase("applet") || nm.equalsIgnoreCase("object") || nm.equalsIgnoreCase("embed")) {
                        if (isObjectTag) {
                            if (atts.get("code") == null && atts.get("object") == null) {
                                statusMsgStream.println(objectRequiresCodeWarning);
                                atts = null;
                            }
                        }
                        if (atts != null) {
                            factory.createAppletViewer(x, y, url, atts);
                            x += XDELTA;
                            y += YDELTA;
                            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                            if ((x > d.width - 300) || (y > d.height - 300)) {
                                x = 0;
                                y = 2 * ydisp * YDELTA;
                                ydisp++;
                            }
                        }
                        atts = null;
                        isAppletTag = false;
                        isObjectTag = false;
                        isEmbedTag = false;
                    }
                } else {
                    String nm = scanIdentifier(in);
                    if (nm.equalsIgnoreCase("param")) {
                        Hashtable t = scanTag(in);
                        String att = (String) t.get("name");
                        if (att == null) {
                            statusMsgStream.println(requiresNameWarning);
                        } else {
                            String val = (String) t.get("value");
                            if (val == null) {
                                statusMsgStream.println(requiresNameWarning);
                            } else if (atts != null) {
                                atts.put(att.toLowerCase(), val);
                            } else {
                                statusMsgStream.println(paramOutsideWarning);
                            }
                        }
                    } else if (nm.equalsIgnoreCase("applet")) {
                        isAppletTag = true;
                        atts = scanTag(in);
                        if (atts.get("code") == null && atts.get("object") == null) {
                            statusMsgStream.println(appletRequiresCodeWarning);
                            atts = null;
                        } else if (atts.get("width") == null) {
                            statusMsgStream.println(appletRequiresWidthWarning);
                            atts = null;
                        } else if (atts.get("height") == null) {
                            statusMsgStream.println(appletRequiresHeightWarning);
                            atts = null;
                        }
                    } else if (nm.equalsIgnoreCase("object")) {
                        isObjectTag = true;
                        atts = scanTag(in);
                        if (atts.get("codebase") != null) {
                            atts.remove("codebase");
                        }
                        if (atts.get("width") == null) {
                            statusMsgStream.println(objectRequiresWidthWarning);
                            atts = null;
                        } else if (atts.get("height") == null) {
                            statusMsgStream.println(objectRequiresHeightWarning);
                            atts = null;
                        }
                    } else if (nm.equalsIgnoreCase("embed")) {
                        isEmbedTag = true;
                        atts = scanTag(in);
                        if (atts.get("code") == null && atts.get("object") == null) {
                            statusMsgStream.println(embedRequiresCodeWarning);
                            atts = null;
                        } else if (atts.get("width") == null) {
                            statusMsgStream.println(embedRequiresWidthWarning);
                            atts = null;
                        } else if (atts.get("height") == null) {
                            statusMsgStream.println(embedRequiresHeightWarning);
                            atts = null;
                        }
                    } else if (nm.equalsIgnoreCase("app")) {
                        statusMsgStream.println(appNotLongerSupportedWarning);
                        Hashtable atts2 = scanTag(in);
                        nm = (String) atts2.get("class");
                        if (nm != null) {
                            atts2.remove("class");
                            atts2.put("code", nm + ".class");
                        }
                        nm = (String) atts2.get("src");
                        if (nm != null) {
                            atts2.remove("src");
                            atts2.put("codebase", nm);
                        }
                        if (atts2.get("width") == null) {
                            atts2.put("width", "100");
                        }
                        if (atts2.get("height") == null) {
                            atts2.put("height", "100");
                        }
                        printTag(statusMsgStream, atts2);
                        statusMsgStream.println();
                    }
                }
            }
        }
        in.close();
    }
