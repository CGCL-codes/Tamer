    public String decodeMessage(ShortMessage message) {
        String strMessage = null;
        switch(message.getCommand()) {
            case 0x80:
                setChanged();
                notifyObservers(message);
                break;
            case 0x90:
                setChanged();
                notifyObservers(message);
                break;
            default:
                strMessage = "unknown message: status = " + message.getStatus() + ", byte1 = " + message.getData1() + ", byte2 = " + message.getData2();
                break;
        }
        if (message.getCommand() != 0xF0) {
            int nChannel = message.getChannel() + 1;
            String strChannel = "channel " + nChannel + ": ";
            strMessage = strChannel + strMessage;
        }
        smCount++;
        smByteCount += message.getLength();
        return "[" + getHexString(message) + "] " + strMessage;
    }
