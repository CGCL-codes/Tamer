    public void render(RenderRequest req, RenderResponse res) throws PortletException, IOException {
        DBBind dbb;
        DBSubset dbs, img;
        JDCConnection con = null;
        ByteArrayInputStream oInStream;
        ByteArrayOutputStream oOutStream;
        OutputStreamWriter oXMLWriter;
        String sCategoryId, sTemplatePath, sLimit, sOffset, sWrkArGet, sWorkAreaId, sImagePath;
        int iOffset = 0, iLimit = 2147483647, iProdCount = 0, iImgCount = 0, iImgIndex;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin ProductList.render()");
            DebugFile.incIdent();
        }
        sOffset = req.getParameter("offset");
        sLimit = req.getParameter("limit");
        sCategoryId = (String) req.getAttribute("category");
        sTemplatePath = (String) req.getAttribute("template");
        sWorkAreaId = req.getProperty("workarea");
        sWrkArGet = req.getProperty("workareasget");
        if (DebugFile.trace) {
            DebugFile.writeln("template=" + sTemplatePath);
            DebugFile.writeln("category=" + sCategoryId);
            DebugFile.writeln("workarea=" + sWorkAreaId);
            DebugFile.writeln("workareasget=" + sWrkArGet);
        }
        try {
            if (null != sOffset) iOffset = Integer.parseInt(sOffset);
        } catch (java.lang.NumberFormatException nfe) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new PortletException("NumberFormatException parameter offset is not a valid integer value", nfe);
        }
        try {
            if (null != sLimit) iLimit = Integer.parseInt(sLimit);
        } catch (java.lang.NumberFormatException nfe) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new PortletException("NumberFormatException parameter limit is not a valid integer value", nfe);
        }
        try {
            dbb = (DBBind) getPortletContext().getAttribute("GlobalDBBind");
            dbs = new DBSubset(DB.k_products + " p," + DB.k_x_cat_objs + " x", "p." + DB.gu_product + ",p." + DB.nm_product + ",p." + DB.de_product + ",p." + DB.pr_list + ",p." + DB.pr_sale + ",p." + DB.id_currency + ",p." + DB.pct_tax_rate + ",p." + DB.is_tax_included + ",p." + DB.dt_start + ",p." + DB.dt_end + ",p." + DB.tag_product + ",p." + DB.id_ref, "p." + DB.gu_product + "=x." + DB.gu_object + " AND x." + DB.id_class + "=15 AND x." + DB.gu_category + "=? ORDER BY x." + DB.od_position, 20);
            con = dbb.getConnection("ProductList");
            if (null != sLimit) dbs.setMaxRows(iLimit);
            if (sOffset == null) iProdCount = dbs.load(con, new Object[] { sCategoryId }); else iProdCount = dbs.load(con, new Object[] { sCategoryId }, iOffset);
        } catch (SQLException sqle) {
            if (DebugFile.trace) DebugFile.writeln("SQLException " + sqle.getMessage());
            if (con != null) {
                try {
                    if (!con.isClosed()) con.close("ProductList");
                } catch (SQLException ignore) {
                }
            }
            if (DebugFile.trace) DebugFile.decIdent();
            throw new PortletException("SQLException " + sqle.getMessage(), sqle);
        }
        if (DebugFile.trace) DebugFile.writeln(String.valueOf(iProdCount) + " products found");
        StringBuffer oXML = new StringBuffer(8192);
        oXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?xml-stylesheet type=\"text/xsl\"?>\n<products count=\"" + String.valueOf(iProdCount) + "\">\n");
        Product oCurrentProd = new Product();
        for (int c = 0; c < iProdCount; c++) {
            oXML.append("  <product>");
            oXML.append("<gu_product>" + dbs.getString(0, c) + "</gu_product><nm_product>" + dbs.getString(1, c) + "</nm_product><tr_product><![CDATA[" + dbs.getStringNull(2, c, "") + "]]></tr_product><de_product><![CDATA[" + dbs.getStringNull(3, c, "") + "]]></de_product>");
            oCurrentProd.replace(DB.gu_product, dbs.getString(0, c));
            oXML.append("<images>");
            try {
                img = oCurrentProd.getImages(con);
                iImgCount = img.getRowCount();
                for (int i = 0; i < iImgCount; i++) {
                    oXML.append("<image tp=\"" + img.getString(DB.tp_image, i) + "\"><gu_image>" + img.getString(DB.gu_image, i) + "</gu_image>");
                    sImagePath = img.getString(DB.path_image, i);
                    oXML.append("<src_image><![CDATA[" + sWrkArGet + "/" + sWorkAreaId + "/apps/Shop/" + sImagePath.substring(sImagePath.indexOf(sWorkAreaId) + 43) + "]]></src_image>");
                    oXML.append("<nm_image><![CDATA[" + img.getStringNull(DB.nm_image, i, "") + "]]></nm_image>");
                    if (img.isNull(DB.dm_width, i)) oXML.append("<dm_width></dm_width>"); else oXML.append("<dm_width>" + img.get(DB.dm_width, i).toString() + "</dm_width>");
                    if (img.isNull(DB.dm_height, i)) oXML.append("<dm_height></dm_height>"); else oXML.append("<dm_height>" + img.get(DB.dm_height, i).toString() + "</dm_height>");
                    oXML.append("<tl_image>" + img.getStringNull(DB.tl_image, i, "") + "</tl_image></image>");
                }
            } catch (SQLException sqle) {
            } catch (NullPointerException npe) {
            }
            oXML.append("</images></product>\n");
        }
        try {
            con.close("ProductList");
            con = null;
        } catch (SQLException sqle) {
            if (DebugFile.trace) DebugFile.writeln("SQLException " + sqle.getMessage());
        }
        oXML.append("</categories>");
        try {
            if (DebugFile.trace) DebugFile.writeln("new ByteArrayInputStream(" + String.valueOf(oXML.length()) + ")");
            oInStream = new ByteArrayInputStream(oXML.toString().getBytes("UTF-8"));
            oOutStream = new ByteArrayOutputStream(40000);
            Properties oProps = new Properties();
            Enumeration oKeys = req.getPropertyNames();
            while (oKeys.hasMoreElements()) {
                String sKey = (String) oKeys.nextElement();
                oProps.setProperty(sKey, req.getProperty(sKey));
            }
            StylesheetCache.transform(sTemplatePath, oInStream, oOutStream, oProps);
            res.getWriter().write(oOutStream.toString("UTF-8"));
            oOutStream.close();
            oInStream.close();
            oInStream = null;
        } catch (TransformerConfigurationException tce) {
            if (DebugFile.trace) {
                DebugFile.writeln("TransformerConfigurationException " + tce.getMessageAndLocation());
                try {
                    DebugFile.write("--------------------------------------------------------------------------------\n");
                    DebugFile.write(FileSystem.readfile(sTemplatePath));
                    DebugFile.write("\n--------------------------------------------------------------------------------\n");
                    DebugFile.write(oXML.toString());
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
                    DebugFile.write(oXML.toString());
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
            DebugFile.writeln("End ProductList.render()");
        }
    }
