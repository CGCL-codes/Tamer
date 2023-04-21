    public static void streamDataResource(OutputStream os, GenericDelegator delegator, String dataResourceId, String https, String webSiteId, Locale locale, String rootDir) throws IOException, GeneralException {
        try {
            GenericValue dataResource = delegator.findByPrimaryKeyCache("DataResource", UtilMisc.toMap("dataResourceId", dataResourceId));
            if (dataResource == null) {
                throw new GeneralException("Error in streamDataResource: DataResource with ID [" + dataResourceId + "] was not found.");
            }
            String dataResourceTypeId = dataResource.getString("dataResourceTypeId");
            if (UtilValidate.isEmpty(dataResourceTypeId)) {
                dataResourceTypeId = "SHORT_TEXT";
            }
            String mimeTypeId = dataResource.getString("mimeTypeId");
            if (UtilValidate.isEmpty(mimeTypeId)) {
                mimeTypeId = "text/html";
            }
            if (dataResourceTypeId.equals("SHORT_TEXT")) {
                String text = dataResource.getString("objectInfo");
                os.write(text.getBytes());
            } else if (dataResourceTypeId.equals("ELECTRONIC_TEXT")) {
                GenericValue electronicText = delegator.findByPrimaryKeyCache("ElectronicText", UtilMisc.toMap("dataResourceId", dataResourceId));
                if (electronicText != null) {
                    String text = electronicText.getString("textData");
                    if (text != null) os.write(text.getBytes());
                }
            } else if (dataResourceTypeId.equals("IMAGE_OBJECT")) {
                byte[] imageBytes = acquireImage(delegator, dataResource);
                if (imageBytes != null) os.write(imageBytes);
            } else if (dataResourceTypeId.equals("LINK")) {
                String text = dataResource.getString("objectInfo");
                os.write(text.getBytes());
            } else if (dataResourceTypeId.equals("URL_RESOURCE")) {
                URL url = new URL(dataResource.getString("objectInfo"));
                if (url.getHost() == null) {
                    String prefix = buildRequestPrefix(delegator, locale, webSiteId, https);
                    String sep = "";
                    if (url.toString().indexOf("/") != 0 && prefix.lastIndexOf("/") != (prefix.length() - 1)) {
                        sep = "/";
                    }
                    String s2 = prefix + sep + url.toString();
                    url = new URL(s2);
                }
                InputStream in = url.openStream();
                int c;
                while ((c = in.read()) != -1) {
                    os.write(c);
                }
            } else if (dataResourceTypeId.indexOf("_FILE") >= 0) {
                String objectInfo = dataResource.getString("objectInfo");
                File inputFile = getContentFile(dataResourceTypeId, objectInfo, rootDir);
                FileInputStream fis = new FileInputStream(inputFile);
                int c;
                while ((c = fis.read()) != -1) {
                    os.write(c);
                }
            } else {
                throw new GeneralException("The dataResourceTypeId [" + dataResourceTypeId + "] is not supported in streamDataResource");
            }
        } catch (GenericEntityException e) {
            throw new GeneralException("Error in streamDataResource", e);
        }
    }
