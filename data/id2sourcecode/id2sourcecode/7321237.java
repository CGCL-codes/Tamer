    public double calculate2DSimilarityBetweenPubchemIds(String first, String second) throws Exception {
        Exception ex = null;
        for (int i = 0; i < MAX_TRY; i++) {
            try {
                PUGLocator locator = new PUGLocator();
                PUGSoap soap = locator.getPUGSoap();
                String key1 = soap.inputList(new int[] { Integer.parseInt(first) }, PCIDType.eID_CID);
                String key2 = soap.inputList(new int[] { Integer.parseInt(second) }, PCIDType.eID_CID);
                String downloadKey = soap.scoreMatrix(key1, key2, ScoreTypeType.eScoreType_Sim2DSubs, MatrixFormatType.eMatrixFormat_IdIdScore, CompressType.eCompress_None);
                StatusType status = waitingForCalculation(soap, downloadKey);
                if (status == StatusType.eStatus_Success) {
                    URL url = new URL(soap.getDownloadUrl(downloadKey));
                    Scanner s = new Scanner(url.openStream());
                    Double result = Double.parseDouble(s.nextLine().split("\t")[2]);
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
