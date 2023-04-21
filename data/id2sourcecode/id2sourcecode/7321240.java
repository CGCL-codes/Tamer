    public String getSmileCodeForPubchemId(String cid) throws Exception {
        Exception ex = null;
        for (int i = 0; i < MAX_TRY; i++) {
            try {
                PUGLocator locator = new PUGLocator();
                PUGSoap soap = locator.getPUGSoap();
                String key = soap.inputList(new int[] { Integer.parseInt(cid) }, PCIDType.eID_CID);
                String downloadKey = soap.download(key, FormatType.eFormat_SMILES, CompressType.eCompress_None, false);
                StatusType status = waitingForCalculation(soap, downloadKey);
                if (status == StatusType.eStatus_Success) {
                    URL url = new URL(soap.getDownloadUrl(downloadKey));
                    Scanner s = new Scanner(url.openStream());
                    String result = s.nextLine().split("\t")[1];
                    return result;
                } else {
                    throw new CompoundNotFoundException(status.getValue() + soap.getStatusMessage(downloadKey));
                }
            } catch (CompoundNotFoundException e) {
                throw e;
            } catch (Exception e) {
                logger.warn(e.getMessage());
                ex = e;
                Thread.sleep(TIME);
            }
        }
        throw ex;
    }
