    public void setTrackingColor(PlayerBlobfinderColorConfig pbcc) {
        try {
            sendHeader(PLAYER_MSGTYPE_REQ, PLAYER_BLOBFINDER_REQ_SET_COLOR, 28);
            XdrBufferEncodingStream xdr = new XdrBufferEncodingStream(28);
            xdr.beginEncoding(null, 0);
            xdr.xdrEncodeInt(pbcc.getChannel());
            xdr.xdrEncodeInt(pbcc.getRmin());
            xdr.xdrEncodeInt(pbcc.getRmax());
            xdr.xdrEncodeInt(pbcc.getGmin());
            xdr.xdrEncodeInt(pbcc.getGmax());
            xdr.xdrEncodeInt(pbcc.getBmin());
            xdr.xdrEncodeInt(pbcc.getBmax());
            xdr.endEncoding();
            os.write(xdr.getXdrData(), 0, xdr.getXdrLength());
            xdr.close();
            os.flush();
        } catch (IOException e) {
            throw new PlayerException("[Blobfinder] : Couldn't request " + "PLAYER_BLOBFINDER_REQ_SET_COLOR: " + e.toString(), e);
        } catch (OncRpcException e) {
            throw new PlayerException("[Blobfinder] : Error while XDR-encoding SET_COLOR request: " + e.toString(), e);
        }
    }
