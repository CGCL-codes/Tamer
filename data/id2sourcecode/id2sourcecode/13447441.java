    public boolean[] getChannels() {
        boolean percussion = false;
        if (getPatch() instanceof ModelPatch) percussion = ((ModelPatch) getPatch()).isPercussion();
        if (percussion) {
            boolean[] ch = new boolean[16];
            for (int i = 0; i < ch.length; i++) ch[i] = false;
            ch[9] = true;
            return ch;
        }
        int bank = getPatch().getBank();
        if (bank >> 7 == 0x78 || bank >> 7 == 0x79) {
            boolean[] ch = new boolean[16];
            for (int i = 0; i < ch.length; i++) ch[i] = true;
            return ch;
        }
        boolean[] ch = new boolean[16];
        for (int i = 0; i < ch.length; i++) ch[i] = true;
        ch[9] = false;
        return ch;
    }
