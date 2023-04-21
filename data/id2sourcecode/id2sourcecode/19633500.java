    public void pause(boolean pause, long time) throws IOException, NetConnectionException, FlvException {
        if (player == null) {
            writeErrorMessage("This channel is already closed");
            return;
        }
        player.pause(pause);
        writeStatusMessage((pause ? "NetStream.Pause.Notify" : "NetStream.Unpause.Notify"), AmfValue.newObject());
    }
