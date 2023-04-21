    private void setContentReader() {
        CharArrayWriter writer = null;
        try {
            writer = new CharArrayWriter();
            if (isNOTEncrypted) {
                try {
                    HSSFWorkbook workbook = new HSSFWorkbook(poiFs, true);
                    HSSFSheet sheet = null;
                    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                        sheet = workbook.getSheetAt(i);
                        Iterator rows = sheet.rowIterator();
                        while (rows.hasNext()) {
                            HSSFRow row = (HSSFRow) rows.next();
                            Iterator cells = row.cellIterator();
                            while (cells.hasNext()) {
                                HSSFCell cell = (HSSFCell) cells.next();
                                switch(cell.getCellType()) {
                                    case HSSFCell.CELL_TYPE_NUMERIC:
                                        String num = Double.toString(cell.getNumericCellValue()).trim();
                                        if (num.length() > 0) {
                                            loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, num + " ");
                                            writer.write(num + " ");
                                        }
                                        break;
                                    case HSSFCell.CELL_TYPE_STRING:
                                        HSSFRichTextString richTextString = cell.getRichStringCellValue();
                                        String text = richTextString.getString();
                                        if (text.length() > 0) {
                                            loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, text + " ");
                                            writer.write(text + " ");
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    reader = new CharArrayReader(writer.toCharArray());
                } catch (RecordFormatException ex) {
                    setContentCorrectness(false);
                } catch (IllegalArgumentException ex) {
                    setContentCorrectness(false);
                } catch (NullPointerException ex) {
                    setContentCorrectness(false);
                }
            }
        } catch (IOException ex) {
            if (reader != null) {
                IOUtils.closeQuietly(reader);
            }
            if (writer != null) {
                IOUtils.closeQuietly(writer);
            }
            loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
        }
    }
