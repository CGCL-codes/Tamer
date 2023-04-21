    public void render(RenderRequest req, RenderResponse res) throws PortletException, IOException {
        ByteArrayInputStream oInStream;
        ByteArrayOutputStream oOutStream;
        OutputStreamWriter oXMLWriter;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin RecentPosts.render()");
            DebugFile.incIdent();
        }
        final int iMaxRecent = 10;
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
        String sCachedFile = "recentpoststab_" + req.getWindowState().toString() + ".xhtm";
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
                        DebugFile.writeln("End RecentPosts.render()");
                    }
                    return;
                }
            } catch (Exception xcpt) {
                DebugFile.writeln(xcpt.getClass().getName() + " " + xcpt.getMessage());
            }
        }
        String sXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\"?>";
        int iPosts = 0;
        if (req.getWindowState().equals(WindowState.MINIMIZED)) {
            sXML += "<posts/>";
        } else {
            DBBind oDBB = (DBBind) getPortletContext().getAttribute("GlobalDBBind");
            DBSubset oPosts = new DBSubset(DB.k_newsmsgs + " m," + DB.k_x_cat_objs + " x," + DB.k_newsgroups + " g", "x." + DB.gu_category + ",m." + DB.gu_msg + ",m." + DB.gu_product + ",m." + DB.nm_author + ",m." + DB.tx_subject + ",m." + DB.dt_published + ",m." + DB.tx_email + ",m." + DB.nu_thread_msgs + ",m." + DB.gu_thread_msg, "m." + DB.id_status + "=0 AND x." + DB.gu_category + "=" + "g." + DB.gu_newsgrp + " AND m." + DB.gu_msg + "=x." + DB.gu_object + " AND m." + DB.gu_parent_msg + " IS NULL AND g." + DB.gu_workarea + "=? ORDER BY 6 DESC", iMaxRecent);
            JDCConnection oCon = null;
            try {
                oCon = oDBB.getConnection("RecentPostsTab");
                iPosts = oPosts.load(oCon, new Object[] { sWorkAreaId });
                oCon.close("RecentPostsTab");
                oCon = null;
                Date dtPub;
                for (int p = 0; p < iPosts; p++) {
                    dtPub = oPosts.getDate(5, p);
                    oPosts.setElementAt(Gadgets.leftPad(String.valueOf(dtPub.getMonth() + 1), '0', 2) + "-" + Gadgets.leftPad(String.valueOf(dtPub.getDate()), '0', 2) + " " + Gadgets.leftPad(String.valueOf(dtPub.getHours()), '0', 2) + ":" + Gadgets.leftPad(String.valueOf(dtPub.getMinutes()), '0', 2), 5, p);
                }
                sXML += "<posts>\n" + oPosts.toXML("", "newsmsg") + "</posts>";
            } catch (SQLException e) {
                sXML += "<posts/>";
                try {
                    if (null != oCon) if (!oCon.isClosed()) oCon.close("RecentPostsTab");
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
            DebugFile.writeln("End RecentPosts.render()");
        }
    }
