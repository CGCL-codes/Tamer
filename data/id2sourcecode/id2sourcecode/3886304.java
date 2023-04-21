    public static Map convertDocumentByteWrapper(DispatchContext dctx, Map context) {
        Map results = ServiceUtil.returnSuccess();
        GenericDelegator delegator = dctx.getDelegator();
        XMultiComponentFactory xmulticomponentfactory = null;
        Timestamp ts = UtilDateTime.nowTimestamp();
        Random random = new Random(ts.getTime());
        String uniqueSeqNum = Integer.toString(Math.abs(random.nextInt()));
        String fileInName = "OOIN_" + uniqueSeqNum;
        String fileOutName = "OOOUT_" + uniqueSeqNum;
        File fileIn = null;
        File fileOut = null;
        ByteWrapper inByteWrapper = (ByteWrapper) context.get("inByteWrapper");
        String inputMimeType = (String) context.get("inputMimeType");
        String outputMimeType = (String) context.get("outputMimeType");
        String extName = OpenOfficeWorker.getExtensionFromMimeType(outputMimeType);
        fileOutName += "." + extName;
        String oooHost = (String) context.get("oooHost");
        String oooPort = (String) context.get("oooPort");
        try {
            xmulticomponentfactory = OpenOfficeWorker.getRemoteServer(oooHost, oooPort);
            byte[] inByteArray = inByteWrapper.getBytes();
            String tempDir = UtilProperties.getPropertyValue("content", "content.temp.dir");
            fileIn = new File(tempDir + fileInName);
            FileOutputStream fos = new FileOutputStream(fileIn);
            fos.write(inByteArray);
            fos.close();
            Debug.logInfo("fileIn:" + tempDir + fileInName, module);
            OpenOfficeWorker.convertOODocToFile(xmulticomponentfactory, tempDir + fileInName, tempDir + fileOutName, outputMimeType);
            fileOut = new File(tempDir + fileOutName);
            FileInputStream fis = new FileInputStream(fileOut);
            int c;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((c = fis.read()) > -1) {
                baos.write(c);
            }
            fis.close();
            results.put("outByteWrapper", new ByteWrapper(baos.toByteArray()));
            baos.close();
        } catch (MalformedURLException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(e.toString());
        } catch (FileNotFoundException e) {
            Debug.logError(e, "Error in OpenOffice operation: ", module);
            return ServiceUtil.returnError(e.toString());
        } catch (IOException e) {
            Debug.logError(e, "Error in OpenOffice operation: ", module);
            return ServiceUtil.returnError(e.toString());
        } catch (Exception e) {
            Debug.logError(e, "Error in OpenOffice operation: ", module);
            return ServiceUtil.returnError(e.toString());
        } finally {
            if (fileIn != null) fileIn.delete();
            if (fileOut != null) fileOut.delete();
        }
        return results;
    }
