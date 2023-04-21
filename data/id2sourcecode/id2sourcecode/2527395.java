    public void render(RenderRequest req, RenderResponse res) throws PortletException, IOException {
        ByteArrayInputStream oInStream;
        ByteArrayOutputStream oOutStream;
        OutputStreamWriter oXMLWriter;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin CallsTab.render()");
            DebugFile.incIdent();
        }
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
        String sCachedFile = "callstab_" + req.getWindowState().toString() + ".xhtm";
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
                        DebugFile.writeln("End CallsTab.render()");
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
            sXML += "<calls/>";
        } else {
            DBBind oDBB = (DBBind) getPortletContext().getAttribute("GlobalDBBind");
            DBSubset oCalls = new DBSubset(DB.k_phone_calls, DB.gu_phonecall + "," + DB.tp_phonecall + "," + DB.dt_start + "," + DB.dt_end + "," + DB.gu_contact + "," + DB.contact_person + "," + DB.tx_phone + "," + DB.tx_comments, DB.gu_workarea + "=? AND " + DB.gu_user + "=? AND " + DB.id_status + "=0 ORDER BY 3 DESC", 10);
            JDCConnection oCon = null;
            try {
                oCon = oDBB.getConnection("CallsTab");
                iCalls = oCalls.load(oCon, new Object[] { sWorkAreaId, sUserId });
                for (int c = 0; c < iCalls; c++) {
                    if (oCalls.isNull(2, c)) oCalls.setElementAt("", 2, c); else {
                        Date dtStart = oCalls.getDate(2, c);
                        oCalls.setElementAt(Gadgets.leftPad(String.valueOf(dtStart.getHours()), '0', 2) + ":" + Gadgets.leftPad(String.valueOf(dtStart.getMinutes()), '0', 2), 2, c);
                    }
                }
                oCon.close("CallsTab");
                oCon = null;
                sXML += "<calls>\n" + oCalls.toXML("", "call") + "</calls>";
            } catch (SQLException e) {
                sXML += "<calls/>";
                try {
                    if (null != oCon) if (!oCon.isClosed()) oCon.close("CallsTab");
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
            DebugFile.writeln("End CallsTab.render()");
        }
    }
