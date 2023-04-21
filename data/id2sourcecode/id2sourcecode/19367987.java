    public void render(RenderRequest req, RenderResponse res) throws PortletException, IOException {
        ByteArrayInputStream oInStream;
        ByteArrayOutputStream oOutStream;
        OutputStreamWriter oXMLWriter;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin RecentContactsTab.render()");
            DebugFile.incIdent();
        }
        final int iMaxRecent = 8;
        FileSystem oFS = new FileSystem(FileSystem.OS_PUREJAVA);
        String sDomainId = req.getProperty("domain");
        String sWorkAreaId = req.getProperty("workarea");
        String sUserId = req.getProperty("user");
        String sZone = req.getProperty("zone");
        String sLang = req.getProperty("language");
        String sTemplatePath = req.getProperty("template");
        String sStorage = req.getProperty("storage");
        String sFileDir = "file://" + sStorage + "domains" + File.separator + sDomainId + File.separator + "workareas" + File.separator + sWorkAreaId + File.separator + "cache" + File.separator + sUserId;
        String sEncoding = res.getCharacterEncoding();
        String sCachedFile = "recentcontactstab_" + req.getWindowState().toString() + ".xhtm";
        if (DebugFile.trace) {
            DebugFile.writeln("user=" + sUserId);
            DebugFile.writeln("template=" + sTemplatePath);
            DebugFile.writeln("cache dir=" + sFileDir);
            DebugFile.writeln("modified=" + req.getAttribute("modified"));
        }
        Date oDtModified = (Date) req.getAttribute("modified");
        if (null != oDtModified) {
            try {
                File oCached = new File(sFileDir.substring(7) + File.separator + sCachedFile);
                if (!oCached.exists()) {
                    oFS.mkdirs(sFileDir);
                } else if (oCached.lastModified() > oDtModified.getTime()) {
                    res.getWriter().write(oFS.readfile(sFileDir + File.separator + sCachedFile, sEncoding == null ? "ISO8859_1" : sEncoding));
                    if (DebugFile.trace) {
                        DebugFile.writeln("cache hit " + sFileDir + File.separator + sCachedFile);
                        DebugFile.decIdent();
                        DebugFile.writeln("End RecentContactsTab.render()");
                    }
                    return;
                }
            } catch (Exception xcpt) {
                DebugFile.writeln(xcpt.getClass().getName() + " " + xcpt.getMessage());
            }
        }
        String sXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\"?>";
        int iCalls = 0;
        if (req.getWindowState().equals(WindowState.MINIMIZED)) {
            sXML += "<contacts/>";
        } else {
            DBBind oDBB = (DBBind) getPortletContext().getAttribute("GlobalDBBind");
            JDCConnection oCon = null;
            try {
                oCon = oDBB.getConnection("RecentContactsTab");
                PreparedStatement oStm = oCon.prepareStatement("(SELECT dt_last_visit,gu_company,NULL AS gu_contact,nm_company,'' AS full_name,work_phone,tx_email FROM k_companies_recent WHERE gu_user=? UNION SELECT dt_last_visit,NULL AS gu_company,gu_contact,nm_company,full_name,work_phone,tx_email FROM k_contacts_recent WHERE gu_user=?) ORDER BY dt_last_visit DESC", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                oStm.setString(1, sUserId);
                oStm.setString(2, sUserId);
                ResultSet oRSet = oStm.executeQuery();
                int iRecentCount = 0;
                StringBuffer oXML = new StringBuffer();
                String sStr;
                while (oRSet.next() && iRecentCount < iMaxRecent) {
                    oXML.append("<contact>");
                    sStr = oRSet.getString(2);
                    if (oRSet.wasNull()) oXML.append("<gu_company/>"); else oXML.append("<gu_company>" + oRSet.getString(2) + "</gu_company>");
                    sStr = oRSet.getString(3);
                    if (oRSet.wasNull()) oXML.append("<gu_contact/>"); else oXML.append("<gu_contact>" + oRSet.getString(3) + "</gu_contact>");
                    sStr = oRSet.getString(4);
                    if (oRSet.wasNull()) oXML.append("<nm_company/>"); else oXML.append("<nm_company><![CDATA[" + oRSet.getString(4) + "]]></nm_company>");
                    sStr = oRSet.getString(5);
                    if (oRSet.wasNull()) oXML.append("<full_name/>"); else oXML.append("<full_name><![CDATA[" + oRSet.getString(5) + "]]></full_name>");
                    sStr = oRSet.getString(6);
                    if (oRSet.wasNull()) oXML.append("<work_phone/>"); else oXML.append("<work_phone><![CDATA[" + oRSet.getString(6) + "]]></work_phone>");
                    sStr = oRSet.getString(7);
                    if (oRSet.wasNull()) oXML.append("<tx_email/>"); else oXML.append("<tx_email>" + oRSet.getString(7) + "</tx_email>");
                    oXML.append("</contact>");
                    iRecentCount++;
                }
                oCon.close("RecentContactsTab");
                oCon = null;
                sXML += "<contacts>\n" + oXML.toString() + "</contacts>";
            } catch (SQLException e) {
                sXML += "<contacts/>";
                try {
                    if (null != oCon) if (!oCon.isClosed()) oCon.close("RecentContactsTab");
                } catch (SQLException ignore) {
                }
            }
        }
        try {
            if (DebugFile.trace) DebugFile.writeln("new ByteArrayInputStream(" + String.valueOf(sXML.length()) + ")");
            if (sEncoding == null) oInStream = new ByteArrayInputStream(sXML.getBytes()); else oInStream = new ByteArrayInputStream(sXML.getBytes(sEncoding));
            oOutStream = new ByteArrayOutputStream(4000);
            Properties oProps = new Properties();
            Enumeration oKeys = req.getPropertyNames();
            while (oKeys.hasMoreElements()) {
                String sKey = (String) oKeys.nextElement();
                oProps.setProperty(sKey, req.getProperty(sKey));
            }
            if (req.getWindowState().equals(WindowState.MINIMIZED)) oProps.setProperty("windowstate", "MINIMIZED"); else oProps.setProperty("windowstate", "NORMAL");
            StylesheetCache.transform(sTemplatePath, oInStream, oOutStream, oProps);
            String sOutput;
            if (sEncoding == null) sOutput = oOutStream.toString(); else sOutput = oOutStream.toString("UTF-8");
            oOutStream.close();
            oInStream.close();
            oInStream = null;
            res.getWriter().write(sOutput);
            oFS.writefilestr(sFileDir + File.separator + sCachedFile, sOutput, sEncoding == null ? "ISO8859_1" : sEncoding);
        } catch (TransformerConfigurationException tce) {
            if (DebugFile.trace) {
                DebugFile.writeln("TransformerConfigurationException " + tce.getMessageAndLocation());
                try {
                    DebugFile.write("--------------------------------------------------------------------------------\n");
                    DebugFile.write(FileSystem.readfile(sTemplatePath));
                    DebugFile.write("\n--------------------------------------------------------------------------------\n");
                    DebugFile.write(sXML);
                    DebugFile.write("\n--------------------------------------------------------------------------------\n");
                } catch (java.io.IOException ignore) {
                } catch (com.enterprisedt.net.ftp.FTPException ignore) {
                }
                DebugFile.decIdent();
            }
            throw new PortletException("TransformerConfigurationException " + tce.getMessage(), tce);
        } catch (TransformerException tex) {
            if (DebugFile.trace) {
                DebugFile.writeln("TransformerException " + tex.getMessageAndLocation());
                try {
                    DebugFile.write("--------------------------------------------------------------------------------\n");
                    DebugFile.write(FileSystem.readfile(sTemplatePath));
                    DebugFile.write("\n--------------------------------------------------------------------------------\n");
                    DebugFile.write(sXML);
                    DebugFile.write("\n--------------------------------------------------------------------------------\n");
                } catch (java.io.IOException ignore) {
                } catch (com.enterprisedt.net.ftp.FTPException ignore) {
                }
                DebugFile.decIdent();
            }
            throw new PortletException("TransformerException " + tex.getMessage(), tex);
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End RecentContactsTab.render()");
        }
    }
