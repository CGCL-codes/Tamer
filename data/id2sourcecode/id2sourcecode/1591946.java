    public static ByteWrapper getInputByteWrapper(Map context, GenericDelegator delegator) throws GeneralException {
        ByteWrapper inputByteWrapper = (ByteWrapper) context.get("inputByteWrapper");
        if (inputByteWrapper == null) {
            String pdfFileNameIn = (String) context.get("pdfFileNameIn");
            String contentId = (String) context.get("contentId");
            if (UtilValidate.isNotEmpty(pdfFileNameIn)) {
                try {
                    FileInputStream fis = new FileInputStream(pdfFileNameIn);
                    int c;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((c = fis.read()) != -1) baos.write(c);
                    inputByteWrapper = new ByteWrapper(baos.toByteArray());
                } catch (FileNotFoundException e) {
                    throw (new GeneralException(e.getMessage()));
                } catch (IOException e) {
                    throw (new GeneralException(e.getMessage()));
                }
            } else if (UtilValidate.isNotEmpty(contentId)) {
                try {
                    Locale locale = (Locale) context.get("locale");
                    String https = (String) context.get("https");
                    String webSiteId = (String) context.get("webSiteId");
                    String rootDir = (String) context.get("rootDir");
                    GenericValue content = delegator.findByPrimaryKeyCache("Content", UtilMisc.toMap("contentId", contentId));
                    String dataResourceId = content.getString("dataResourceId");
                    inputByteWrapper = DataResourceWorker.getContentAsByteWrapper(delegator, dataResourceId, https, webSiteId, locale, rootDir);
                } catch (GenericEntityException e) {
                    throw (new GeneralException(e.getMessage()));
                } catch (IOException e) {
                    throw (new GeneralException(e.getMessage()));
                }
            }
        }
        return inputByteWrapper;
    }
