    public static void streamDataResource(OutputStream os, CmsOFBizRemoteClient remoteClient, String dataResourceId, String https, String webSiteId, Locale locale, String rootDir) throws IOException, GeneralException {
        try {
            GenericValue dataResource = remoteClient.findByPrimaryKeyCache("DataResource", UtilMisc.toMap("dataResourceId", dataResourceId));
            if (dataResource == null) {
                throw new GeneralException("Error in streamDataResource: DataResource with ID [" + dataResourceId + "] was not found.");
            }
            Map fields = dataResource.getAllFields();
            String dataResourceTypeId = (String) fields.get("dataResourceTypeId");
            if (UtilValidate.isEmpty(dataResourceTypeId)) {
                dataResourceTypeId = "SHORT_TEXT";
            }
            String mimeTypeId = (String) fields.get("mimeTypeId");
            if (UtilValidate.isEmpty(mimeTypeId)) {
                mimeTypeId = "text/html";
            }
            if (dataResourceTypeId.equals("SHORT_TEXT")) {
                String text = (String) fields.get("objectInfo");
                os.write(text.getBytes());
            } else if (dataResourceTypeId.equals("ELECTRONIC_TEXT")) {
                GenericValue electronicText = remoteClient.findByPrimaryKeyCache("ElectronicText", UtilMisc.toMap("dataResourceId", dataResourceId));
                if (electronicText != null) {
                    fields = electronicText.getAllFields();
                    String text = (String) fields.get("textData");
                    if (text != null) os.write(text.getBytes());
                }
            } else if (dataResourceTypeId.equals("IMAGE_OBJECT")) {
                byte[] imageBytes = acquireImage(remoteClient, dataResource);
                if (imageBytes != null) os.write(imageBytes);
            } else if (dataResourceTypeId.equals("LINK")) {
                String text = (String) fields.get("objectInfo");
                os.write(text.getBytes());
            } else if (dataResourceTypeId.equals("URL_RESOURCE")) {
                URL url = new URL((String) fields.get("objectInfo"));
                if (url.getHost() == null) {
                    String prefix = buildRequestPrefix(remoteClient, locale, webSiteId, https);
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
                String objectInfo = (String) fields.get("objectInfo");
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
