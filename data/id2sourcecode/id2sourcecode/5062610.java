    public static Any createExcelFile(Connection con, Any values, Any retValue) throws SQLException, IOException, ParserConfigurationException, SAXException, StandardException {
        HashMap val = (HashMap) values.extract_Value();
        String sForm = (String) val.get("FORM");
        final int MAX_ROWS = Integer.valueOf(ResourceBundle.getBundle(ExcelReports.class.getName()).getString("excelMaxRows"));
        String sXMLFiles = ResourceBundle.getBundle(ExcelReports.class.getName()).getString("XMLFiles");
        InputStream is = ExcelReports.class.getResourceAsStream(sXMLFiles + "/" + sForm);
        ReportSAXParser parser = new ReportSAXParser();
        if (!parser.isValidXML(is)) {
            throw (new StandardException("XML File is not valid"));
        }
        is.close();
        is = ExcelReports.class.getResourceAsStream(sXMLFiles + "/" + sForm);
        parser.parse(is);
        HSSFWorkbook wb = new HSSFWorkbook();
        {
            HSSFSheet sheet = null;
            String[] parameters = null;
            PreparedStatement stmt = null;
            int iRow = 0;
            if (parser.m_Header != null && parser.m_Header.sSheet != null) sheet = wb.createSheet(parser.m_Header.sSheet); else sheet = wb.createSheet("Report_Sheet0");
            if (parser.m_Header != null) try {
                if (parser.m_Header.picture != null) {
                    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, parser.m_Header.picture.col1, parser.m_Header.picture.row1, parser.m_Header.picture.col2, parser.m_Header.picture.row2);
                    anchor.setAnchorType(2);
                    patriarch.createPicture(anchor, loadPicture(new FileInputStream(parser.m_Header.picture.sSrc), wb));
                }
                parameters = findParameters(parser.m_Header.sQuery);
                stmt = con.prepareStatement(parser.m_Header.sQuery);
                int iPar = 0;
                for (String parName : parameters) {
                    Object o = val.get(parName);
                    iPar++;
                    setParameter(stmt, iPar, o);
                }
                ResultSet rSet = stmt.executeQuery();
                ResultSetMetaData data = rSet.getMetaData();
                if (rSet.next()) {
                    for (ReportSAXParser.Row r : parser.m_Header.arRows) {
                        HSSFRow row = sheet.getRow(r.iNum);
                        if (row == null) row = sheet.createRow(r.iNum);
                        iRow = iRow < r.iNum ? r.iNum : iRow;
                        for (ReportSAXParser.Column c : r.arColumns) {
                            if (c.iWidth > 0) sheet.setColumnWidth(c.iNum, (short) (c.iWidth * 256));
                            if (c.iColspan > 1) sheet.addMergedRegion(new Region(r.iNum, c.iNum, r.iNum, (short) (c.iNum + c.iColspan - 1)));
                            parser.writeCell(row, data, rSet, c, wb, (short) -1);
                        }
                    }
                }
                rSet.close();
            } finally {
                if (stmt != null) stmt.close();
            }
            int iSheet = 1;
            iRow++;
            for (ReportSAXParser.Rows pRow : parser.m_Rows) {
                try {
                    stmt = con.prepareStatement(pRow.sQuery);
                    parameters = findParameters(pRow.sQuery);
                    int iPar = 0;
                    for (String parName : parameters) {
                        Object o = val.get(parName);
                        iPar++;
                        setParameter(stmt, iPar, o);
                    }
                    HSSFPalette palette = wb.getCustomPalette();
                    HSSFColor colHeader = palette.findSimilarColor(pRow.crHeaderBack[0], pRow.crHeaderBack[1], pRow.crHeaderBack[2]);
                    palette.setColorAtIndex(colHeader.getIndex(), pRow.crHeaderBack[0], pRow.crHeaderBack[1], pRow.crHeaderBack[2]);
                    colHeader = palette.getColor(colHeader.getIndex());
                    HSSFColor colEven = palette.findSimilarColor(pRow.crEvenBack[0], pRow.crEvenBack[1], pRow.crEvenBack[2]);
                    palette.setColorAtIndex(colEven.getIndex(), pRow.crEvenBack[0], pRow.crEvenBack[1], pRow.crEvenBack[2]);
                    colEven = palette.getColor(colEven.getIndex());
                    HSSFColor colOgg = palette.findSimilarColor(pRow.crOggBack[0], pRow.crOggBack[1], pRow.crOggBack[2]);
                    palette.setColorAtIndex(colOgg.getIndex(), pRow.crOggBack[0], pRow.crOggBack[1], pRow.crOggBack[2]);
                    colOgg = palette.getColor(colOgg.getIndex());
                    iRow = pRow.iStartRow > iRow ? pRow.iStartRow : iRow;
                    if (pRow.sSheet != null) {
                        sheet = wb.getSheet(pRow.sSheet);
                        if (sheet == null) {
                            sheet = wb.createSheet(pRow.sSheet);
                            iRow = pRow.iStartRow;
                        }
                    }
                    HSSFRow row = sheet.getRow(iRow);
                    if (row == null) row = sheet.createRow(iRow);
                    for (ReportSAXParser.Column c : pRow.arColumns) {
                        String sOldFont = c.sFont;
                        short iOldFont = c.iFontSize;
                        if (pRow.sHeaderFont != null) {
                            c.sFont = pRow.sHeaderFont;
                            c.iFontSize = pRow.iHeaderFontSize;
                        }
                        HSSFCellStyle newStyle = parser.createStyle(c, wb, colHeader.getIndex(), HSSFDataFormat.getBuiltinFormat("text"));
                        newStyle.setWrapText(true);
                        if (c.iWidth > 0) sheet.setColumnWidth(c.iNum, (short) (c.iWidth * 256));
                        if (c.iColspan > 1) sheet.addMergedRegion(new Region(iRow, c.iNum, iRow, (short) (c.iNum + c.iColspan - 1)));
                        HSSFCell cell = row.createCell(c.iNum);
                        cell.setCellValue(new HSSFRichTextString(c.sLabel));
                        cell.setCellStyle(newStyle);
                        if (pRow.sHeaderFont != null) {
                            c.sFont = sOldFont;
                            c.iFontSize = iOldFont;
                        }
                    }
                    iRow++;
                    ResultSet rSet = stmt.executeQuery();
                    ResultSetMetaData data = rSet.getMetaData();
                    boolean bHasRows = false;
                    while (rSet.next()) {
                        bHasRows = true;
                        iRow = pRow.iStartRow > iRow ? pRow.iStartRow : iRow;
                        row = sheet.getRow(iRow);
                        if (row == null) row = sheet.createRow(iRow);
                        for (ReportSAXParser.Column c : pRow.arColumns) {
                            short colIndex = -1;
                            if (iRow % 2 == 0) {
                                colIndex = colEven.getIndex();
                            } else {
                                colIndex = colOgg.getIndex();
                            }
                            if (c.iWidth > 0) sheet.setColumnWidth(c.iNum, (short) (c.iWidth * 256));
                            if (c.iColspan > 1) sheet.addMergedRegion(new Region(iRow, c.iNum, iRow, (short) (c.iNum + c.iColspan - 1)));
                            if (c.sFormat != null && c.sFormat.length() > 0) parser.writeCell(row, data, rSet, c, wb, colIndex);
                        }
                        iRow++;
                        if (iRow == MAX_ROWS) {
                            sheet = wb.createSheet("Report_Sheet" + iSheet);
                            iRow = 0;
                            iSheet++;
                            continue;
                        }
                    }
                    rSet.close();
                    if (!bHasRows) {
                        iRow = pRow.iStartRow > iRow ? pRow.iStartRow : iRow;
                        row = sheet.getRow(iRow);
                        if (row == null) row = sheet.createRow(iRow);
                        for (ReportSAXParser.Column c : pRow.arColumns) {
                            short colIndex = -1;
                            if (iRow % 2 == 0) {
                                colIndex = colEven.getIndex();
                            } else {
                                colIndex = colOgg.getIndex();
                            }
                            if (c.iWidth > 0) sheet.setColumnWidth(c.iNum, (short) (c.iWidth * 256));
                            if (c.iColspan > 1) sheet.addMergedRegion(new Region(iRow, c.iNum, iRow, (short) (c.iNum + c.iColspan - 1)));
                            if (c.sFormat != null && c.sFormat.length() > 0) {
                                parser.writeCell(row, null, null, c, wb, colIndex);
                            }
                        }
                        iRow++;
                    }
                } finally {
                    if (stmt != null) stmt.close();
                }
            }
            if (parser.m_Footer != null) try {
                stmt = con.prepareStatement(parser.m_Footer.sQuery);
                int iPar = 0;
                for (String parName : parameters) {
                    Object o = val.get(parName);
                    iPar++;
                    setParameter(stmt, iPar, o);
                }
                ResultSet rSet = stmt.executeQuery();
                ResultSetMetaData data = rSet.getMetaData();
                if (rSet.next()) {
                    for (ReportSAXParser.Row r : parser.m_Footer.arRows) {
                        HSSFRow row = sheet.getRow(iRow + r.iNum);
                        if (row == null) row = sheet.createRow(iRow + r.iNum);
                        for (ReportSAXParser.Column c : r.arColumns) {
                            if (c.iWidth > 0) sheet.setColumnWidth(c.iNum, (short) (c.iWidth * 256));
                            if (c.iColspan > 1) sheet.addMergedRegion(new Region(iRow + r.iNum, c.iNum, iRow + r.iNum, (short) (c.iNum + c.iColspan - 1)));
                            parser.writeCell(row, data, rSet, c, wb, (short) -1);
                        }
                    }
                }
                rSet.close();
            } finally {
                if (stmt != null) stmt.close();
            }
        }
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapMaxSize = Runtime.getRuntime().maxMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        System.out.println("Freing memory FreeSize: " + heapFreeSize + " ....");
        System.gc();
        heapFreeSize = Runtime.getRuntime().freeMemory();
        System.out.println("OK FreeSize: " + heapFreeSize + " ....");
        ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
        GZIPOutputStream gs = new GZIPOutputStream(fileOut);
        wb.write(gs);
        gs.finish();
        fileOut.close();
        System.gc();
        retValue.insert_Value(fileOut.toByteArray());
        return retValue;
    }
