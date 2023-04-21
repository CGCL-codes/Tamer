    public boolean generate(int type, Song song, String sourceName, String path, String name, String tmpPath, int addr, boolean debug, int f4Table) {
        int maxInstr;
        String outFileName;
        String inFileName;
        String res;
        String extension = "";
        String exDefine = "";
        String exFormat = "";
        switch(type) {
            case TYPE_SID:
                extension = ".sid";
                exDefine = "-DEX_SID=1";
                exFormat = "-f3";
                break;
            case TYPE_PRG:
                extension = ".prg";
                exDefine = "-DEX_SID=2";
                exFormat = "-f1";
                break;
            case TYPE_BIN:
                extension = ".bin";
                exDefine = "-DEX_SID=3";
                exFormat = "-f3";
                break;
        }
        String exDefineTune = "-DNUM_TUNE=" + song.getNumberOfTunes();
        String exDefinePat = "-DNUM_PAT=" + song.getMaxPattern();
        maxInstr = song.getMaxInstrument();
        String exDefineInstr = "-DNUM_INSTR=" + maxInstr;
        outFileName = path + File.separator + name + extension;
        inFileName = tmpPath + File.separator + sourceName;
        try {
            URL url = getClass().getResource("/jitt64/asm/player.s");
            File outputFile = new File(inFileName);
            InputStream in = url.openStream();
            FileWriter out = new FileWriter(outputFile);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            for (int i = 1; i <= maxInstr; i++) {
                res = getAsmFullInstrument(song.getInstruments()[i - 1], i).toString();
                out.write(res);
            }
            res = getAsmFullTracks(song).toString();
            out.write(res);
            res = getAsmFullPatterns(song).toString();
            out.write(res);
            res = getPSIDStrings(song);
            out.write(res);
            in.close();
            out.close();
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
        String[] args;
        if (debug) args = new String[37]; else args = new String[36];
        args[0] = " ";
        args[1] = inFileName;
        args[2] = "-o" + outFileName;
        args[3] = exDefine;
        args[4] = exDefineTune;
        args[5] = exDefinePat;
        args[6] = exDefineInstr;
        args[7] = exFormat;
        args[8] = "-DUSE_INSTR_AD=" + song.getUseInstrTable(Song.INSTR_AD);
        args[9] = "-DUSE_INSTR_SR=" + song.getUseInstrTable(Song.INSTR_SR);
        args[10] = "-DUSE_INSTR_WAVE=" + song.getUseInstrTable(Song.INSTR_WAVE);
        args[11] = "-DUSE_INSTR_FREQ=" + song.getUseInstrTable(Song.INSTR_FREQ);
        args[12] = "-DUSE_INSTR_PULSE=" + song.getUseInstrTable(Song.INSTR_PULSE);
        args[13] = "-DUSE_INSTR_FILTER=" + song.getUseInstrTable(Song.INSTR_FILTER);
        args[14] = "-DUSE_INSTR_RES=" + song.getUseInstrTable(Song.INSTR_RES);
        args[15] = "-DUSE_INSTR_TYPE=" + song.getUseInstrTable(Song.INSTR_TYPE);
        args[16] = "-DA4_FREQ=" + f4Table;
        args[17] = "-DEX_BASE=" + addr;
        args[18] = "-DEX_SPEED=" + song.getIntSpeed();
        args[19] = "-DEX_CHIP=" + song.getIntChip();
        args[20] = "-DUSE_CMD_TEMPO=" + song.getUsePatternCmd(Song.CMD_TEMPO);
        args[21] = "-DUSE_CMD_AD=" + song.getUsePatternCmd(Song.CMD_AD);
        args[22] = "-DUSE_CMD_SR=" + song.getUsePatternCmd(Song.CMD_SR);
        args[23] = "-DUSE_CMD_VOLUME=" + song.getUsePatternCmd(Song.CMD_VOLUME);
        args[24] = "-DUSE_CMD_ARP=" + song.getUsePatternCmd(Song.CMD_ARP);
        args[25] = "-DUSE_CMD_PUP=" + song.getUsePatternCmd(Song.CMD_PUP);
        args[26] = "-DUSE_CMD_PDN=" + song.getUsePatternCmd(Song.CMD_PDN);
        args[27] = "-DUSE_CMD_TPO=" + song.getUsePatternCmd(Song.CMD_TPO);
        args[28] = "-DUSE_CMD_VIB=" + song.getUsePatternCmd(Song.CMD_VIB);
        args[29] = "-DUSE_CMD_SUP=" + song.getUsePatternCmd(Song.CMD_SUP);
        args[30] = "-DUSE_CMD_SDN=" + song.getUsePatternCmd(Song.CMD_SDN);
        args[31] = "-DUSE_CMD_FOUT=" + song.getUsePatternCmd(Song.CMD_FOUT);
        args[32] = "-DUSE_CMD_FTY=" + song.getUsePatternCmd(Song.CMD_FTY);
        args[33] = "-DUSE_CMD_FRS=" + song.getUsePatternCmd(Song.CMD_FRS);
        args[34] = "-DUSE_CMD_FCU=" + song.getUsePatternCmd(Song.CMD_FCU);
        args[35] = "-DUSE_CMD_GSR=" + song.getUsePatternCmd(Song.CMD_GSR);
        if (debug) args[36] = "-L" + tmpPath + File.separator + "out.txt";
        try {
            Class cl = Class.forName("jitt64.asm.Main");
            Method mMain = cl.getMethod("run", new Class[] { String[].class });
            mMain.invoke(cl.newInstance(), new Object[] { args });
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
        return true;
    }
