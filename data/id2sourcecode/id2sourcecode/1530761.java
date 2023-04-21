    public void render(RenderRequest req, RenderResponse res) throws PortletException, IOException {
        ByteArrayInputStream oInStream;
        ByteArrayOutputStream oOutStream;
        OutputStreamWriter oXMLWriter;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin CalendarTab.render()");
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
        String sCachedFile = "calendartab_" + req.getWindowState().toString() + ".xhtm";
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
                        DebugFile.writeln("End CalendarTab.render()");
                    }
                    return;
                }
            } catch (Exception xcpt) {
                DebugFile.writeln(xcpt.getClass().getName() + " " + xcpt.getMessage());
            }
        }
        String sXML;
        int iToDo = 0, iMeetings = 0;
        if (req.getWindowState().equals(WindowState.MINIMIZED)) {
            sXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\"?><calendar><todo/><today/></calendar>";
        } else {
            String sTodayXML, sToDoXML;
            Date dt00 = new Date();
            Date dt23 = new Date();
            dt00.setHours(0);
            dt00.setMinutes(0);
            dt00.setSeconds(0);
            dt23.setHours(23);
            dt23.setMinutes(59);
            dt23.setSeconds(59);
            DBBind oDBB = (DBBind) getPortletContext().getAttribute("GlobalDBBind");
            DBSubset oToDo = new DBSubset(DB.k_to_do, DB.gu_to_do + "," + DB.od_priority + "," + DB.tl_to_do, DB.gu_user + "=? AND (" + DB.tx_status + "='PENDING' OR " + DB.tx_status + " IS NULL) ORDER BY 2 DESC", 10);
            DBSubset oMeetings = new DBSubset(DB.k_meetings + " m," + DB.k_x_meeting_fellow + " f", "m." + DB.gu_meeting + ",m." + DB.gu_fellow + ",m." + DB.tp_meeting + ",m." + DB.tx_meeting + ", m." + DB.dt_start + ",m." + DB.dt_end, "m." + DB.gu_meeting + "=f." + DB.gu_meeting + " AND f." + DB.gu_fellow + "=? AND m." + DB.dt_start + " BETWEEN ? AND ? ORDER BY m." + DB.dt_start, 10);
            JDCConnection oCon = null;
            try {
                oCon = oDBB.getConnection("CalendarTab_today");
                oToDo.setMaxRows(10);
                iToDo = oToDo.load(oCon, new Object[] { sUserId });
                for (int a = 0; a < iToDo; a++) {
                    if (oToDo.getStringNull(2, a, "").length() > 40) oToDo.setElementAt(oToDo.getString(2, a).substring(0, 40) + "...", 2, a);
                }
                sToDoXML = oToDo.toXML("", "activity");
                iMeetings = oMeetings.load(oCon, new Object[] { sUserId, new Timestamp(dt00.getTime()), new Timestamp(dt23.getTime()) });
                for (int m = 0; m < iMeetings; m++) {
                    if (oMeetings.isNull(3, m)) oMeetings.setElementAt("untitled", 3, m);
                    Date oFrom = oMeetings.getDate(4, m);
                    oMeetings.setElementAt(String.valueOf(oFrom.getHours()) + ":" + Gadgets.leftPad(String.valueOf(oFrom.getMinutes()), '0', 2), 4, m);
                    Date oTo = oMeetings.getDate(5, m);
                    oMeetings.setElementAt(String.valueOf(oTo.getHours()) + ":" + Gadgets.leftPad(String.valueOf(oTo.getMinutes()), '0', 2), 5, m);
                }
                oCon.close("CalendarTab_today");
                oCon = null;
                sTodayXML = oMeetings.toXML("", "meeting");
            } catch (SQLException e) {
                sToDoXML = "<todo></todo>";
                sTodayXML = "<today></today>";
                try {
                    if (null != oCon) if (!oCon.isClosed()) oCon.close("CalendarTab_today");
                } catch (SQLException ignore) {
                }
            }
            sXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?xml-stylesheet type=\"text/xsl\"?>\n<calendar>\n";
            if (iToDo > 0) sXML += "<todo>\n" + sToDoXML + "</todo>\n"; else sXML += "<todo/>\n";
            if (iMeetings > 0) sXML += "<today>\n" + sTodayXML + "</today>\n</calendar>"; else sXML += "<today/>\n</calendar>";
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
            DebugFile.writeln("End CalendarTab.render()");
        }
    }
