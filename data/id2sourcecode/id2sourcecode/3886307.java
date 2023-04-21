    public static Map convertDocumentStreamToStream(DispatchContext dctx, Map context) {
        XMultiComponentFactory xmulticomponentfactory = null;
        String stringUrl = "file:///" + context.get("filenameFrom");
        String stringConvertedFile = "file:///" + context.get("filenameTo");
        String inputMimeType = (String) context.get("inputMimeType");
        String outputMimeType = (String) context.get("outputMimeType");
        String oooHost = (String) context.get("oooHost");
        String oooPort = (String) context.get("oooPort");
        try {
            xmulticomponentfactory = OpenOfficeWorker.getRemoteServer(oooHost, oooPort);
            File inputFile = new File(stringUrl);
            long fileSize = inputFile.length();
            FileInputStream fis = new FileInputStream(inputFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream((int) fileSize);
            int c;
            while ((c = fis.read()) != -1) {
                baos.write(c);
            }
            OpenOfficeByteArrayInputStream oobais = new OpenOfficeByteArrayInputStream(baos.toByteArray());
            OpenOfficeByteArrayOutputStream oobaos = OpenOfficeWorker.convertOODocByteStreamToByteStream(xmulticomponentfactory, oobais, inputMimeType, outputMimeType);
            FileOutputStream fos = new FileOutputStream(stringConvertedFile);
            fos.write(oobaos.toByteArray());
            fos.close();
            fis.close();
            oobais.close();
            oobaos.close();
            Map results = ServiceUtil.returnSuccess();
            return results;
        } catch (IOException e) {
            Debug.logError(e, "Error in OpenOffice operation: ", module);
            return ServiceUtil.returnError(e.toString());
        } catch (Exception e) {
            Debug.logError(e, "Error in OpenOffice operation: ", module);
            return ServiceUtil.returnError(e.toString());
        }
    }
