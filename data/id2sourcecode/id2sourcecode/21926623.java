    public void setEvelope(Envelope e) {
        setBoundsInMicros(e.getTOn(), e.getTOff());
        gain = e.getGain();
        attackEnd = startByte + milliToByte(e.getTRise());
        decayStart = endByte - milliToByte(e.getTFall());
        if (attackEnd > decayStart) {
            long av = (attackEnd + decayStart) / 2;
            attackEnd = decayStart = (av / (nChannels * 2)) * nChannels * 2;
        }
    }
