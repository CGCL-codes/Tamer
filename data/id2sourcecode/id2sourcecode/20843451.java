    public static Map<String, Object> download(DispatchContext dctx, Map<String, Object> context) {
        Map<String, Object> result;
        String templateName = (String) context.get("templateName");
        String partyId = (String) context.get(ACCOUNT_ENTITY_ID);
        GenericDelegator delegator = dctx.getDelegator();
        List<GenericValue> reportTemplate = null;
        try {
            reportTemplate = delegator.findList("ReportTemplate", new EntityConditionList<EntityExpr>(UtilMisc.<EntityExpr>toList(new EntityExpr("templateName", EntityOperator.EQUALS, templateName), new EntityExpr("partyId", EntityOperator.EQUALS, partyId)), EntityOperator.AND), UtilMisc.toSet("fileName", "fileContentType"), null, null, false);
        } catch (GenericEntityException e) {
            e.printStackTrace();
        }
        String fileName = dctx.getClass().getResource("/runtime/upload").getPath();
        fileName += "/" + reportTemplate.get(0).get("fileName");
        File templateFile = new File(fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(templateFile);
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
        result.put("templateFile", bb);
        result.put("fileContentType", reportTemplate.get(0).get("fileContentType"));
        return result;
    }
