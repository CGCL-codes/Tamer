    public static Map getDataResourceStream(GenericValue dataResource, String https, String webSiteId, Locale locale, String contextRoot, boolean cache) throws IOException, GeneralException {
        if (dataResource == null) {
            throw new GeneralException("Cannot stream null data resource!");
        }
        String dataResourceTypeId = dataResource.getString("dataResourceTypeId");
        String dataResourceId = dataResource.getString("dataResourceId");
        GenericDelegator delegator = dataResource.getDelegator();
        if (dataResourceTypeId.endsWith("_TEXT") || "LINK".equals(dataResourceTypeId)) {
            String text = "";
            if ("SHORT_TEXT".equals(dataResourceTypeId) || "LINK".equals(dataResourceTypeId)) {
                text = dataResource.getString("objectInfo");
            } else if ("ELECTRONIC_TEXT".equals(dataResourceTypeId)) {
                GenericValue electronicText;
                if (cache) {
                    electronicText = delegator.findByPrimaryKeyCache("ElectronicText", UtilMisc.toMap("dataResourceId", dataResourceId));
                } else {
                    electronicText = delegator.findByPrimaryKey("ElectronicText", UtilMisc.toMap("dataResourceId", dataResourceId));
                }
                if (electronicText != null) {
                    text = electronicText.getString("textData");
                }
            } else {
                throw new GeneralException("Unsupported TEXT type; cannot stream");
            }
            byte[] bytes = text.getBytes();
            return UtilMisc.toMap("stream", new ByteArrayInputStream(bytes), "length", new Integer(bytes.length));
        } else if (dataResourceTypeId.endsWith("_OBJECT")) {
            byte[] bytes = new byte[0];
            GenericValue valObj;
            if ("IMAGE_OBJECT".equals(dataResourceTypeId)) {
                if (cache) {
                    valObj = delegator.findByPrimaryKeyCache("ImageDataResource", UtilMisc.toMap("dataResourceId", dataResourceId));
                } else {
                    valObj = delegator.findByPrimaryKey("ImageDataResource", UtilMisc.toMap("dataResourceId", dataResourceId));
                }
                if (valObj != null) {
                    bytes = valObj.getBytes("imageData");
                }
            } else if ("VIDEO_OBJECT".equals(dataResourceTypeId)) {
                if (cache) {
                    valObj = delegator.findByPrimaryKeyCache("VideoDataResource", UtilMisc.toMap("dataResourceId", dataResourceId));
                } else {
                    valObj = delegator.findByPrimaryKey("VideoDataResource", UtilMisc.toMap("dataResourceId", dataResourceId));
                }
                if (valObj != null) {
                    bytes = valObj.getBytes("videoData");
                }
            } else if ("AUDIO_OBJECT".equals(dataResourceTypeId)) {
                if (cache) {
                    valObj = delegator.findByPrimaryKeyCache("AudioDataResource", UtilMisc.toMap("dataResourceId", dataResourceId));
                } else {
                    valObj = delegator.findByPrimaryKey("AudioDataResource", UtilMisc.toMap("dataResourceId", dataResourceId));
                }
                if (valObj != null) {
                    bytes = valObj.getBytes("audioData");
                }
            } else if ("OTHER_OBJECT".equals(dataResourceTypeId)) {
                if (cache) {
                    valObj = delegator.findByPrimaryKeyCache("OtherDataResource", UtilMisc.toMap("dataResourceId", dataResourceId));
                } else {
                    valObj = delegator.findByPrimaryKey("OtherDataResource", UtilMisc.toMap("dataResourceId", dataResourceId));
                }
                if (valObj != null) {
                    bytes = valObj.getBytes("dataResourceContent");
                }
            } else {
                throw new GeneralException("Unsupported OBJECT type [" + dataResourceTypeId + "]; cannot stream");
            }
            return UtilMisc.toMap("stream", new ByteArrayInputStream(bytes), "length", new Long(bytes.length));
        } else if (dataResourceTypeId.endsWith("_FILE") || dataResourceTypeId.endsWith("_FILE_BIN")) {
            String objectInfo = dataResource.getString("objectInfo");
            if (UtilValidate.isNotEmpty(objectInfo)) {
                File file = DataResourceWorker.getContentFile(dataResourceTypeId, objectInfo, contextRoot);
                return UtilMisc.toMap("stream", new FileInputStream(file), "length", new Long(file.length()));
            } else {
                throw new GeneralException("No objectInfo found for FILE type [" + dataResourceTypeId + "]; cannot stream");
            }
        } else if ("URL_RESOURCE".equals(dataResourceTypeId)) {
            String objectInfo = dataResource.getString("objectInfo");
            if (UtilValidate.isNotEmpty(objectInfo)) {
                URL url = new URL(objectInfo);
                if (url.getHost() == null) {
                    String newUrl = DataResourceWorker.buildRequestPrefix(delegator, locale, webSiteId, https);
                    if (!newUrl.endsWith("/")) {
                        newUrl = newUrl + "/";
                    }
                    newUrl = newUrl + url.toString();
                    url = new URL(newUrl);
                }
                URLConnection con = url.openConnection();
                return UtilMisc.toMap("stream", con.getInputStream(), "length", new Long(con.getContentLength()));
            } else {
                throw new GeneralException("No objectInfo found for URL_RESOURCE type; cannot stream");
            }
        }
        throw new GeneralException("The dataResourceTypeId [" + dataResourceTypeId + "] is not supported in getDataResourceStream");
    }
