    public void setAudioProperties(PlayerAudiodspConfig paconfig) {
        try {
            sendHeader(PLAYER_MSGTYPE_REQ, PLAYER_AUDIODSP_SET_CONFIG, 12);
            XdrBufferEncodingStream xdr = new XdrBufferEncodingStream(12);
            xdr.beginEncoding(null, 0);
            xdr.xdrEncodeInt(paconfig.getFormat());
            xdr.xdrEncodeFloat(paconfig.getFrequency());
            xdr.xdrEncodeInt(paconfig.getChannels());
            xdr.endEncoding();
            os.write(xdr.getXdrData(), 0, xdr.getXdrLength());
            xdr.close();
            os.flush();
        } catch (IOException e) {
            throw new PlayerException("[AudioDSP] : Couldn't send PLAYER_AUDIODSP_SET_CONFIG " + "request: " + e.toString(), e);
        } catch (OncRpcException e) {
            throw new PlayerException("[AudioDSP] : Error while XDR-encoding SET_CONFIG request: " + e.toString(), e);
        }
    }
