    private String getOrConvertCSVCellValue(XThread userThread, ContextUElem pContextUElem, ContextUCon pContextUCon, XPathResult cellValue, Sheet.Column column) throws ExBadPath {
        String cellStrValue = cellValue.asString();
        DOM resultDOM = cellValue.asResultDOMOrNull();
        NodeInfo nodeInfo = resultDOM != null ? userThread.getTopModule().getNodeInfo(resultDOM) : null;
        String columnDataType = column.getType();
        if (cellStrValue == null) cellStrValue = "";
        if (columnDataType == null && nodeInfo != null) {
            String moduleDataType = nodeInfo.getDataTypeIntern();
            int colonPos = moduleDataType.indexOf(":");
            if (colonPos >= 0) moduleDataType = moduleDataType.substring(colonPos + 1);
            if (schemaIntegerTypeNamesSet.contains(moduleDataType)) columnDataType = "integer"; else if (schemaRealTypeNamesSet.contains(moduleDataType)) columnDataType = "real"; else if (schemaDateTimeTypeNamesSet.contains(moduleDataType)) columnDataType = moduleDataType.toLowerCase();
        }
        if ("integer".equalsIgnoreCase(columnDataType) || "real".equalsIgnoreCase(columnDataType)) {
            Number number = null;
            try {
                number = cellValue.asNumber();
                String excelFormatPattern = XFUtil.nvl(column.getOutputFormatSpecification(), ("real".equalsIgnoreCase(columnDataType) ? "0.00" : "0"));
                DecimalFormat df = new DecimalFormat(excelFormatPattern);
                cellStrValue = df.format(number.doubleValue());
            } catch (Throwable ignoreTh) {
            }
        } else if ("boolean".equalsIgnoreCase(columnDataType)) {
            Number number = null;
            try {
                cellStrValue = cellValue.asBoolean() ? "true" : "false";
            } catch (Throwable ignoreTh) {
            }
        } else if ("date".equalsIgnoreCase(columnDataType) || "time".equalsIgnoreCase(columnDataType) || "datetime".equalsIgnoreCase(columnDataType)) {
            try {
                String inputDateFormat = column.getInputFormatSpecification();
                if (inputDateFormat == null) {
                    if ("date".equalsIgnoreCase(columnDataType)) inputDateFormat = "yyyy-MM-dd"; else if ("time".equalsIgnoreCase(columnDataType)) inputDateFormat = "HH:mm:ss"; else if ("datetime".equalsIgnoreCase(columnDataType)) inputDateFormat = "yyyy-MM-dd'T'HH:mm:ss"; else inputDateFormat = "yyyy-MM-dd";
                }
                SimpleDateFormat df = new SimpleDateFormat(inputDateFormat);
                Date inputDate = df.parse(cellValue.asString());
                String excelFormatPattern = column.getOutputFormatSpecification();
                if (excelFormatPattern == null) {
                    if ("date".equalsIgnoreCase(columnDataType)) excelFormatPattern = "dd-MMM-YYYY"; else if ("time".equalsIgnoreCase(columnDataType)) excelFormatPattern = "hh:mm:ss a"; else if ("datetime".equalsIgnoreCase(columnDataType)) excelFormatPattern = "dd-MMM-YYYY hh:mm:ss a"; else excelFormatPattern = "dd-MMM-YYYY";
                }
                df.applyPattern(excelFormatPattern);
                cellStrValue = df.format(inputDate);
            } catch (Throwable ignoreTh) {
            }
        }
        return escapeCSVFieldValue(cellStrValue);
    }
