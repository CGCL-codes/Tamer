    private static void encodeReplyOption(BearerData bData, BitwiseOutputStream outStream) throws BitwiseOutputStream.AccessException {
        outStream.write(8, 1);
        outStream.write(1, bData.userAckReq ? 1 : 0);
        outStream.write(1, bData.deliveryAckReq ? 1 : 0);
        outStream.write(1, bData.readAckReq ? 1 : 0);
        outStream.write(1, bData.reportReq ? 1 : 0);
        outStream.write(4, 0);
    }
