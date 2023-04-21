    public void runExcelGenerate(String outputType, XThread userThread, ContextUElem pContextUElem, ContextUCon pContextUCon) {
        String fileExtension = "." + outputType.toLowerCase();
        String contentType = ("CSV".equalsIgnoreCase(outputType) ? "text/csv" : "application/vnd.ms-excel");
        String popupWindowName = getUniqueWindowName();
        UCon lUCon = null;
        BLOB lBlob = null;
        CLOB lClob = null;
        try {
            lUCon = pContextUCon.getUCon();
            Writer writer = null;
            OutputStream blobOS = null;
            String lFileName = "generated-" + XFUtil.unique() + fileExtension;
            if (!XFUtil.isNull(mFileName)) {
                try {
                    lFileName = pContextUElem.extendedStringOrXPathString(pContextUElem.attachDOM(), mFileName) + fileExtension;
                } catch (ExActionFailed e) {
                    throw new ExInternal("Filename XPath is invalid: " + mFileName, e);
                }
            }
            String lTempResourceId = userThread.createTempWorkDocId();
            if ("CSV".equalsIgnoreCase(outputType)) {
                try {
                    lClob = lUCon.getTemporaryClob();
                    try {
                        writer = lClob.getCharacterOutputStream();
                    } catch (SQLException e) {
                        throw new ExInternal("ERROR", e);
                    }
                    for (int s = 0; s < sheets.length; s++) {
                        boolean visibleColumns[] = new boolean[sheets[s].getColumns().length];
                        boolean lShowHeaders;
                        if (XFUtil.isNull(sheets[s].getShowHeadersExpr())) {
                            lShowHeaders = true;
                        } else {
                            lShowHeaders = pContextUElem.extendedXPathBoolean(pContextUElem.attachDOM(), sheets[s].getShowHeadersExpr());
                        }
                        for (int c = 0; c < sheets[s].getColumns().length; c++) {
                            Sheet.Column column = sheets[s].getColumns()[c];
                            visibleColumns[c] = column.getVisibleExpression() == null || pContextUElem.extendedXPathBoolean(pContextUElem.attachDOM(), column.getVisibleExpression());
                        }
                        for (int c = 0; c < sheets[s].getColumns().length; c++) {
                            Sheet.Column column = sheets[s].getColumns()[c];
                            String cellValue = pContextUElem.extendedStringOrXPathString(pContextUElem.attachDOM(), column.getNameExpr());
                            if (lShowHeaders && visibleColumns[c]) {
                                writer.write(escapeCSVFieldValue(cellValue));
                                if (c < sheets[s].getColumns().length - 1 && visibleColumns[c + 1]) writer.write(",");
                            }
                        }
                        if (lShowHeaders) {
                            writer.write(System.getProperty("line.separator", "\r\n"));
                        }
                        DOMList rowElems = pContextUElem.extendedXPathUL(sheets[s].getRowExpression(), ContextUElem.ATTACH);
                        for (int r = 0; r < rowElems.getLength(); r++) {
                            DOM rowElem = rowElems.item(r);
                            for (int c = 0; c < sheets[s].getColumns().length; c++) {
                                if (!visibleColumns[c]) continue;
                                Sheet.Column column = sheets[s].getColumns()[c];
                                XPathResult cellValue = pContextUElem.extendedXPathResult(rowElem, column.getColumnExpression());
                                writer.write(getOrConvertCSVCellValue(userThread, pContextUElem, pContextUCon, cellValue, column));
                                if (c < sheets[s].getColumns().length - 1 && visibleColumns[c + 1]) writer.write(",");
                            }
                            writer.write(System.getProperty("line.separator", "\r\n"));
                        }
                    }
                } finally {
                    IOUtil.close(writer);
                }
            } else if ("XLS".equalsIgnoreCase(outputType)) {
                try {
                    lBlob = lUCon.getTemporaryBlob();
                    blobOS = lBlob.getBinaryOutputStream();
                    HSSFWorkbook wb = new HSSFWorkbook();
                    for (int s = 0; s < sheets.length; s++) {
                        HSSFSheet sheet = wb.createSheet(pContextUElem.extendedStringOrXPathString(pContextUElem.attachDOM(), sheets[s].getName()));
                        boolean visibleColumns[] = new boolean[sheets[s].getColumns().length];
                        boolean lShowHeaders;
                        if (XFUtil.isNull(sheets[s].getShowHeadersExpr())) {
                            lShowHeaders = false;
                        } else {
                            lShowHeaders = pContextUElem.extendedXPathBoolean(pContextUElem.attachDOM(), sheets[s].getShowHeadersExpr());
                        }
                        for (int c = 0; c < sheets[s].getColumns().length; c++) {
                            Sheet.Column column = sheets[s].getColumns()[c];
                            visibleColumns[c] = column.getVisibleExpression() == null || pContextUElem.extendedXPathBoolean(pContextUElem.attachDOM(), column.getVisibleExpression());
                        }
                        int rowOffset = 0;
                        HSSFRow headerRow = null;
                        if (lShowHeaders) {
                            headerRow = sheet.createRow(0);
                            rowOffset++;
                        }
                        for (int c = 0, visCols = 0; c < sheets[s].getColumns().length; c++) {
                            Sheet.Column column = sheets[s].getColumns()[c];
                            String cellValue = pContextUElem.extendedStringOrXPathString(pContextUElem.attachDOM(), column.getNameExpr());
                            if (lShowHeaders && visibleColumns[c]) {
                                HSSFCell cell = headerRow.createCell((short) visCols);
                                cell.setCellValue(cellValue);
                            }
                            visCols++;
                        }
                        DOMList rowElems = pContextUElem.extendedXPathUL(sheets[s].getRowExpression(), ContextUElem.ATTACH);
                        for (int r = 0; r < rowElems.getLength(); r++) {
                            DOM rowElem = rowElems.item(r);
                            HSSFRow row = sheet.createRow(r + rowOffset);
                            for (int c = 0, visCols = 0; c < sheets[s].getColumns().length; c++) {
                                Sheet.Column column = sheets[s].getColumns()[c];
                                XPathResult cellValue = pContextUElem.extendedXPathResult(rowElem, column.getColumnExpression());
                                if (!visibleColumns[c]) continue;
                                HSSFCell cell = row.createCell((short) visCols);
                                setXLSCellValue(userThread, pContextUElem, pContextUCon, wb, cell, cellValue, column);
                                visCols++;
                            }
                        }
                    }
                    wb.write(blobOS);
                } finally {
                    IOUtil.close(blobOS);
                }
            }
            if (mMethod.equals("url") || mMethod.equals("preview")) {
                if (lBlob != null) {
                    userThread.getTopApp().createTemporaryResource(lTempResourceId, "4", "4", lBlob, contentType, lUCon);
                } else if (lClob != null) {
                    userThread.getTopApp().createTemporaryResource(lTempResourceId, "4", "4", lClob, contentType, lUCon);
                }
                StreamParcel lStreamParcel;
                try {
                    lStreamParcel = new StreamParcel(StreamParcel.TYPE_STANDARD, lFileName, userThread.getTopApp(), userThread.getFoxRequest().getCookieValue("streamsession"));
                } catch (ExServiceUnavailable e) {
                    throw new ExInternal("Unable to get App for creating Stream Parcel: ", e);
                }
                StreamParcelInput lStreamParcelInput = new StreamParcelInputTempResource(lFileName, null, contentType, lTempResourceId);
                lStreamParcel.addStreamParcelInput(lStreamParcelInput, userThread);
                userThread.addURIFilePopup(lStreamParcel.getStreamURL(userThread.getFoxRequest()), lFileName);
            } else if (mMethod.equalsIgnoreCase("storage-location")) {
                FileStorageLocation sl = userThread.getTopModule().getFileStorageLocation(mStoreLocationName);
                if (sl == null) {
                    throw new ExInternal("Unable to locate file storage location, \"" + mStoreLocationName + "\", in module \"" + userThread.getTopModule().getName() + "\" - " + "please update the module specification and ensure one exists.");
                }
                WorkingFileStoreLocation workingSL = new WorkingFileStoreLocation(sl, pContextUElem);
                if (lBlob != null) {
                    workingSL.updateLOB(lBlob, pContextUCon, new FileUploadInfo());
                } else if (lClob != null) {
                    workingSL.updateLOB(lClob, pContextUCon, new FileUploadInfo());
                }
            }
        } catch (Exception ioEx) {
            throw new ExInternal("An unexpected error occurred during Excel generation: ", ioEx);
        } finally {
            try {
                lBlob.close();
            } catch (Throwable ignore) {
            }
            try {
                lClob.close();
            } catch (Throwable ignore) {
            }
            try {
                lUCon.freeTemporaryBlob(lBlob);
            } catch (Throwable ignore) {
            }
            try {
                lUCon.freeTemporaryClob(lClob);
            } catch (Throwable ignore) {
            }
        }
    }
