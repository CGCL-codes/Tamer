    public void mixerMultiChannels(PlayerAudioMixerChannelList pacmd) {
        try {
            int size = 8 + pacmd.getChannels_count() * 12;
            sendHeader(PLAYER_MSGTYPE_CMD, PLAYER_AUDIO_CMD_MIXER_CHANNEL, size);
            XdrBufferEncodingStream xdr = new XdrBufferEncodingStream(size);
            xdr.beginEncoding(null, 0);
            xdr.xdrEncodeInt(pacmd.getChannels_count());
            xdr.xdrEncodeInt(pacmd.getChannels_count());
            PlayerAudioMixerChannel[] channels = pacmd.getChannels();
            for (int i = 0; i < channels.length; i++) {
                xdr.xdrEncodeFloat(channels[i].getAmplitude());
                xdr.xdrEncodeBoolean(channels[i].getActive());
                xdr.xdrEncodeInt(channels[i].getIndex());
            }
            xdr.endEncoding();
            os.write(xdr.getXdrData(), 0, xdr.getXdrLength());
            xdr.close();
            os.flush();
        } catch (IOException e) {
            throw new PlayerException("[Audio] : Couldn't send command: " + e.toString(), e);
        } catch (OncRpcException e) {
            throw new PlayerException("[Audio] : Error while XDR-encoding command: " + e.toString(), e);
        }
    }
