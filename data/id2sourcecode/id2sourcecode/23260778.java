    public static Map renderCompDocPdf(DispatchContext dctx, Map context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String rootDir = (String) context.get("rootDir");
        String webSiteId = (String) context.get("webSiteId");
        String https = (String) context.get("https");
        GenericDelegator delegator = dctx.getDelegator();
        String contentId = (String) context.get("contentId");
        String contentRevisionSeqId = (String) context.get("contentRevisionSeqId");
        String oooHost = (String) context.get("oooHost");
        String oooPort = (String) context.get("oooPort");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        try {
            Timestamp nowTimestamp = UtilDateTime.nowTimestamp();
            List exprList = new ArrayList();
            exprList.add(new EntityExpr("contentIdTo", EntityOperator.EQUALS, contentId));
            exprList.add(new EntityExpr("rootRevisionContentId", EntityOperator.EQUALS, contentId));
            if (UtilValidate.isNotEmpty(contentRevisionSeqId)) {
                exprList.add(new EntityExpr("contentRevisionSeqId", EntityOperator.LESS_THAN_EQUAL_TO, contentRevisionSeqId));
            }
            exprList.add(new EntityExpr("contentAssocTypeId", EntityOperator.EQUALS, "COMPDOC_PART"));
            exprList.add(new EntityExpr("fromDate", EntityOperator.LESS_THAN_EQUAL_TO, nowTimestamp));
            List thruList = new ArrayList();
            thruList.add(new EntityExpr("thruDate", EntityOperator.EQUALS, null));
            thruList.add(new EntityExpr("thruDate", EntityOperator.GREATER_THAN, nowTimestamp));
            exprList.add(new EntityConditionList(thruList, EntityOperator.OR));
            EntityConditionList conditionList = new EntityConditionList(exprList, EntityOperator.AND);
            String[] fields = { "rootRevisionContentId", "itemContentId", "maxRevisionSeqId", "contentId", "dataResourceId", "contentIdTo", "contentAssocTypeId", "fromDate", "sequenceNum" };
            List selectFields = UtilMisc.toListArray(fields);
            List orderByFields = UtilMisc.toList("sequenceNum");
            List compDocParts = delegator.findByCondition("ContentAssocRevisionItemView", conditionList, selectFields, orderByFields);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            document.setPageSize(PageSize.LETTER);
            PdfCopy writer = new PdfCopy(document, baos);
            document.open();
            Iterator iter = compDocParts.iterator();
            int pgCnt = 0;
            while (iter.hasNext()) {
                GenericValue contentAssocRevisionItemView = (GenericValue) iter.next();
                String thisDataResourceId = contentAssocRevisionItemView.getString("dataResourceId");
                GenericValue dataResource = delegator.findByPrimaryKey("DataResource", UtilMisc.toMap("dataResourceId", thisDataResourceId));
                String inputMimeType = null;
                if (dataResource != null) {
                    inputMimeType = dataResource.getString("mimeTypeId");
                }
                byte[] inputByteArray = null;
                PdfReader reader = null;
                if (inputMimeType != null && inputMimeType.equals("application/pdf")) {
                    ByteWrapper byteWrapper = DataResourceWorker.getContentAsByteWrapper(delegator, thisDataResourceId, https, webSiteId, locale, rootDir);
                    inputByteArray = byteWrapper.getBytes();
                    reader = new PdfReader(inputByteArray);
                } else if (inputMimeType != null && inputMimeType.equals("text/html")) {
                    ByteWrapper byteWrapper = DataResourceWorker.getContentAsByteWrapper(delegator, thisDataResourceId, https, webSiteId, locale, rootDir);
                    inputByteArray = byteWrapper.getBytes();
                    String s = new String(inputByteArray);
                    Debug.logInfo("text/html string:" + s, module);
                    continue;
                } else if (inputMimeType != null && inputMimeType.equals("application/vnd.ofbiz.survey.response")) {
                    String surveyResponseId = dataResource.getString("relatedDetailId");
                    String surveyId = null;
                    String acroFormContentId = null;
                    GenericValue surveyResponse = null;
                    if (UtilValidate.isNotEmpty(surveyResponseId)) {
                        surveyResponse = delegator.findByPrimaryKey("SurveyResponse", UtilMisc.toMap("surveyResponseId", surveyResponseId));
                        if (surveyResponse != null) {
                            surveyId = surveyResponse.getString("surveyId");
                        }
                    }
                    if (UtilValidate.isNotEmpty(surveyId)) {
                        GenericValue survey = delegator.findByPrimaryKey("Survey", UtilMisc.toMap("surveyId", surveyId));
                        if (survey != null) {
                            acroFormContentId = survey.getString("acroFormContentId");
                            if (UtilValidate.isNotEmpty(acroFormContentId)) {
                            }
                        }
                    }
                    if (surveyResponse != null) {
                        if (UtilValidate.isEmpty(acroFormContentId)) {
                            Map survey2PdfResults = dispatcher.runSync("buildPdfFromSurveyResponse", UtilMisc.toMap("surveyResponseId", surveyId));
                            if (ServiceUtil.isError(survey2PdfResults)) {
                                return ServiceUtil.returnError("Error building PDF from SurveyResponse: ", null, null, survey2PdfResults);
                            }
                            ByteWrapper outByteWrapper = (ByteWrapper) survey2PdfResults.get("outByteWrapper");
                            inputByteArray = outByteWrapper.getBytes();
                            reader = new PdfReader(inputByteArray);
                        } else {
                            Map survey2AcroFieldResults = dispatcher.runSync("setAcroFieldsFromSurveyResponse", UtilMisc.toMap("surveyResponseId", surveyResponseId));
                            if (ServiceUtil.isError(survey2AcroFieldResults)) {
                                return ServiceUtil.returnError("Error setting AcroFields from SurveyResponse: ", null, null, survey2AcroFieldResults);
                            }
                            ByteWrapper outByteWrapper = (ByteWrapper) survey2AcroFieldResults.get("outByteWrapper");
                            inputByteArray = outByteWrapper.getBytes();
                            reader = new PdfReader(inputByteArray);
                        }
                    }
                } else {
                    ByteWrapper inByteWrapper = DataResourceWorker.getContentAsByteWrapper(delegator, thisDataResourceId, https, webSiteId, locale, rootDir);
                    Map convertInMap = UtilMisc.toMap("userLogin", userLogin, "inByteWrapper", inByteWrapper, "inputMimeType", inputMimeType, "outputMimeType", "application/pdf");
                    if (UtilValidate.isNotEmpty(oooHost)) convertInMap.put("oooHost", oooHost);
                    if (UtilValidate.isNotEmpty(oooPort)) convertInMap.put("oooPort", oooPort);
                    Map convertResult = dispatcher.runSync("convertDocumentByteWrapper", convertInMap);
                    if (ServiceUtil.isError(convertResult)) {
                        return ServiceUtil.returnError("Error in Open", null, null, convertResult);
                    }
                    ByteWrapper outByteWrapper = (ByteWrapper) convertResult.get("outByteWrapper");
                    inputByteArray = outByteWrapper.getBytes();
                    reader = new PdfReader(inputByteArray);
                }
                if (reader != null) {
                    int n = reader.getNumberOfPages();
                    for (int i = 0; i < n; i++) {
                        PdfImportedPage pg = writer.getImportedPage(reader, i + 1);
                        writer.addPage(pg);
                        pgCnt++;
                    }
                }
            }
            document.close();
            ByteWrapper outByteWrapper = new ByteWrapper(baos.toByteArray());
            Map results = ServiceUtil.returnSuccess();
            results.put("outByteWrapper", outByteWrapper);
            return results;
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError(e.toString());
        } catch (IOException e) {
            Debug.logError(e, "Error in CompDoc operation: ", module);
            return ServiceUtil.returnError(e.toString());
        } catch (Exception e) {
            Debug.logError(e, "Error in CompDoc operation: ", module);
            return ServiceUtil.returnError(e.toString());
        }
    }
