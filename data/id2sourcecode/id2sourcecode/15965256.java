    public Decoder(BitStream objBS, Header objFrameHeader, int wch) {
        objSynt = new Synthesis(objFrameHeader.getChannels());
        switch(objFrameHeader.getLayer()) {
            case 1:
                layer123 = new Layer1(objBS, objFrameHeader, objSynt, wch);
                break;
            case 2:
                layer123 = new Layer2(objBS, objFrameHeader, objSynt, wch);
                break;
            case 3:
                layer123 = new Layer3(objBS, objFrameHeader, objSynt, wch);
                break;
        }
    }
