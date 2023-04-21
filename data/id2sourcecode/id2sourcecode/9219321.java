    @Override
    public void runProcessRoutine() {
        int count = 0;
        if (getChannel() == null) {
            return;
        }
        try {
            byte[] bytes = new byte[1024];
            _buffer = ByteBuffer.wrap(bytes);
            assert _buffer.hasArray() && _buffer.isDirect();
            _byteInputStream = new ByteArrayInputStream(bytes);
            while ((count = getChannel().read(_buffer)) > 0) {
                _objInputStream = new testInputOutputByteBuffer().new MyObjectInputStream(_byteInputStream);
                IStreamPacket packet = (IStreamPacket) _objInputStream.readObject();
                while (packet != null) {
                    PushDataReturnValue returnValue = null;
                    if (packet instanceof IMetaDataPacket) {
                        returnValue = broadcastMetadata((IMetaDataPacket) packet);
                    } else {
                    }
                    _objInputStream.readStreamHeader();
                    packet = (IStreamPacket) _objInputStream.readObject();
                }
                _buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (count < 0) {
        }
    }
