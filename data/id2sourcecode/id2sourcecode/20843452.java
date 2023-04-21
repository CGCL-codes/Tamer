    public static Map<String, Object> processReport(DispatchContext dctx, Map<String, Object> context) {
        Map<String, Object> result = null;
        String partyId = (String) context.get(ACCOUNT_ENTITY_ID);
        String templateName = (String) context.get("templateName");
        GenericDelegator delegator = dctx.getDelegator();
        List<GenericValue> reportTemplate = null;
        try {
            reportTemplate = delegator.findList("ReportTemplate", new EntityConditionList<EntityExpr>(UtilMisc.<EntityExpr>toList(new EntityExpr("templateName", EntityOperator.EQUALS, templateName), new EntityExpr("partyId", EntityOperator.EQUALS, partyId)), EntityOperator.AND), UtilMisc.toSet("fileName", "templateTypeId", "fileContentType"), null, null, false);
        } catch (GenericEntityException e) {
            e.printStackTrace();
        }
        if (reportTemplate == null || reportTemplate.size() == 0) return ServiceUtil.returnError("Can't find template!");
        String fileName = dctx.getClass().getResource("/runtime/upload").getPath();
        fileName += "/" + reportTemplate.get(0).get("fileName");
        File templateFile = new File(fileName);
        String templateType = (String) reportTemplate.get(0).get("templateTypeId");
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String glFiscalTypeId = (String) context.get("glFiscalTypeId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map<String, Object> resultMap = null;
        Map<String, Object> inputMap = null;
        if (templateType.equals(TYPE_ONE)) {
            try {
                resultMap = dispatcher.runSync("prepareBalanceSheetData", UtilMisc.toMap("organizationPartyId", partyId, "thruDate", context.get("thruDate"), "glFiscalTypeId", glFiscalTypeId, "userLogin", userLogin));
            } catch (GenericServiceException e) {
                e.printStackTrace();
            }
            inputMap = (Map<String, Object>) resultMap.get("balancesMap");
        } else if (templateType.equals(TYPE_TWO)) {
            try {
                resultMap = dispatcher.runSync("prepareIncomeStatementData", UtilMisc.toMap("organizationPartyId", partyId, "fromDate", context.get("fromDate"), "thruDate", context.get("thruDate"), "glFiscalTypeId", glFiscalTypeId, "userLogin", userLogin));
            } catch (GenericServiceException e) {
                e.printStackTrace();
            }
            inputMap = (Map<String, Object>) resultMap.get("glAccountTotalsMap");
        }
        SpreadSheetParser ssParser = null;
        String contentType = (String) reportTemplate.get(0).get("fileContentType");
        if (contentType.equals("application/vnd.oasis.opendocument.spreadsheet")) ssParser = new ODSSpreadSheetParser(templateFile, inputMap); else if (contentType.equals("application/vnd.ms-excel")) ssParser = new XLSSpreadSheetParser(templateFile, inputMap); else return ServiceUtil.returnError("Unsupport file format");
        File output = ssParser.getParsedFile();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileChannel fc = fis.getChannel();
        ByteBuffer bb = null;
        try {
            bb = ByteBuffer.allocate((int) fc.size());
            fc.read(bb);
            fc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = ServiceUtil.returnSuccess("Donwload Template Success");
        result.put("parsedFile", bb);
        result.put("fileContentType", contentType);
        return result;
    }
