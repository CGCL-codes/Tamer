    public Layer3(BitStream bs, Header h, Synthesis filter, int wch) {
        objInBitStream = bs;
        objHeader = h;
        intChannels = objHeader.getChannels();
        intWhichChannel = wch;
        intMaxGr = (objHeader.getVersion() == Header.MPEG1) ? 2 : 1;
        objFilter = filter;
        objSI = new SideInfo();
        objHuffBits = new HuffmanBits(objInBitStream);
        objSideBS = new BitStream(36);
        scfL = new int[2][23];
        scfS = new int[2][3][13];
        is = new int[32 * 18 + 4];
        xr = new float[2][32][18];
        intWidthLong = new int[22];
        intWidthShort = new int[13];
        floatRawOut = new float[36];
        floatPrevBlck = new float[2][32][18];
        cs = new float[] { 0.857492925712f, 0.881741997318f, 0.949628649103f, 0.983314592492f, 0.995517816065f, 0.999160558175f, 0.999899195243f, 0.999993155067f };
        ca = new float[] { -0.5144957554270f, -0.4717319685650f, -0.3133774542040f, -0.1819131996110f, -0.0945741925262f, -0.0409655828852f, -0.0141985685725f, -0.00369997467375f };
        int i;
        floatPowIS = new float[8207];
        for (i = 0; i < 8207; i++) floatPowIS[i] = (float) Math.pow(i, 4.0 / 3.0);
        floatPow2 = new float[256 + 118 + 4];
        for (i = -256; i < 118 + 4; i++) floatPow2[i + 256] = (float) Math.pow(2.0, -0.25 * (i + 210));
        if (intChannels == 2) switch(intWhichChannel) {
            case Decoder.CH_LEFT:
                intFirstChannel = intLastChannel = 0;
                break;
            case Decoder.CH_RIGHT:
                intFirstChannel = intLastChannel = 1;
                break;
            case Decoder.CH_BOTH:
            default:
                intFirstChannel = 0;
                intLastChannel = 1;
                break;
        } else intFirstChannel = intLastChannel = 0;
        int intSfreq = objHeader.getSampleFrequency();
        intSfreq += (objHeader.getVersion() == Header.MPEG1) ? 0 : ((objHeader.getVersion() == Header.MPEG2) ? 3 : 6);
        switch(intSfreq) {
            case 0:
                intSfbIdxLong = new int[] { 0, 4, 8, 12, 16, 20, 24, 30, 36, 44, 52, 62, 74, 90, 110, 134, 162, 196, 238, 288, 342, 418, 576 };
                intSfbIdxShort = new int[] { 0, 4, 8, 12, 16, 22, 30, 40, 52, 66, 84, 106, 136, 192 };
                break;
            case 1:
                intSfbIdxLong = new int[] { 0, 4, 8, 12, 16, 20, 24, 30, 36, 42, 50, 60, 72, 88, 106, 128, 156, 190, 230, 276, 330, 384, 576 };
                intSfbIdxShort = new int[] { 0, 4, 8, 12, 16, 22, 28, 38, 50, 64, 80, 100, 126, 192 };
                break;
            case 2:
                intSfbIdxLong = new int[] { 0, 4, 8, 12, 16, 20, 24, 30, 36, 44, 54, 66, 82, 102, 126, 156, 194, 240, 296, 364, 448, 550, 576 };
                intSfbIdxShort = new int[] { 0, 4, 8, 12, 16, 22, 30, 42, 58, 78, 104, 138, 180, 192 };
                break;
            case 3:
                intSfbIdxLong = new int[] { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576 };
                intSfbIdxShort = new int[] { 0, 4, 8, 12, 18, 24, 32, 42, 56, 74, 100, 132, 174, 192 };
                break;
            case 4:
                intSfbIdxLong = new int[] { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 114, 136, 162, 194, 232, 278, 330, 394, 464, 540, 576 };
                intSfbIdxShort = new int[] { 0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 136, 180, 192 };
                break;
            case 5:
                intSfbIdxLong = new int[] { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576 };
                intSfbIdxShort = new int[] { 0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 134, 174, 192 };
                break;
            case 6:
                intSfbIdxLong = new int[] { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576 };
                intSfbIdxShort = new int[] { 0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 134, 174, 192 };
                break;
            case 7:
                intSfbIdxLong = new int[] { 0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576 };
                intSfbIdxShort = new int[] { 0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 134, 174, 192 };
                break;
            case 8:
                intSfbIdxLong = new int[] { 0, 12, 24, 36, 48, 60, 72, 88, 108, 132, 160, 192, 232, 280, 336, 400, 476, 566, 568, 570, 572, 574, 576 };
                intSfbIdxShort = new int[] { 0, 8, 16, 24, 36, 52, 72, 96, 124, 160, 162, 164, 166, 192 };
                break;
        }
        for (i = 0; i < 22; i++) intWidthLong[i] = intSfbIdxLong[i + 1] - intSfbIdxLong[i];
        for (i = 0; i < 13; i++) intWidthShort[i] = intSfbIdxShort[i + 1] - intSfbIdxShort[i];
        boolIntensityStereo = objHeader.isIStereo();
        if (boolIntensityStereo) {
            if (objHeader.getVersion() == Header.MPEG1) is_coef = new float[] { 0.0f, 0.211324865f, 0.366025404f, 0.5f, 0.633974596f, 0.788675135f, 1.0f }; else lsf_is_coef = new float[][] { { 0.840896415f, 0.707106781f, 0.594603558f, 0.5f, 0.420448208f, 0.353553391f, 0.297301779f, 0.25f, 0.210224104f, 0.176776695f, 0.148650889f, 0.125f, 0.105112052f, 0.088388348f, 0.074325445f }, { 0.707106781f, 0.5f, 0.353553391f, 0.25f, 0.176776695f, 0.125f, 0.088388348f, 0.0625f, 0.044194174f, 0.03125f, 0.022097087f, 0.015625f, 0.011048543f, 0.0078125f, 0.005524272f } };
        }
        if (objHeader.getVersion() != Header.MPEG1) {
            i_slen2 = new int[256];
            n_slen2 = new int[512];
            slen_tab2 = new byte[][][] { { { 6, 5, 5, 5 }, { 6, 5, 7, 3 }, { 11, 10, 0, 0 }, { 7, 7, 7, 0 }, { 6, 6, 6, 3 }, { 8, 8, 5, 0 } }, { { 9, 9, 9, 9 }, { 9, 9, 12, 6 }, { 18, 18, 0, 0 }, { 12, 12, 12, 0 }, { 12, 9, 9, 6 }, { 15, 12, 9, 0 } }, { { 6, 9, 9, 9 }, { 6, 9, 12, 6 }, { 15, 18, 0, 0 }, { 6, 15, 12, 0 }, { 6, 12, 9, 6 }, { 6, 18, 9, 0 } } };
            int j, k, l, n;
            for (i = 0; i < 5; i++) for (j = 0; j < 6; j++) for (k = 0; k < 6; k++) {
                n = k + j * 6 + i * 36;
                i_slen2[n] = i | (j << 3) | (k << 6) | (3 << 12);
            }
            for (i = 0; i < 4; i++) for (j = 0; j < 4; j++) for (k = 0; k < 4; k++) {
                n = k + j * 4 + i * 16;
                i_slen2[n + 180] = i | (j << 3) | (k << 6) | (4 << 12);
            }
            for (i = 0; i < 4; i++) for (j = 0; j < 3; j++) {
                n = j + i * 3;
                i_slen2[n + 244] = i | (j << 3) | (5 << 12);
                n_slen2[n + 500] = i | (j << 3) | (2 << 12) | (1 << 15);
            }
            for (i = 0; i < 5; i++) for (j = 0; j < 5; j++) for (k = 0; k < 4; k++) for (l = 0; l < 4; l++) {
                n = l + k * 4 + j * 16 + i * 80;
                n_slen2[n] = i | (j << 3) | (k << 6) | (l << 9);
            }
            for (i = 0; i < 5; i++) for (j = 0; j < 5; j++) for (k = 0; k < 4; k++) {
                n = k + j * 4 + i * 20;
                n_slen2[n + 400] = i | (j << 3) | (k << 6) | (1 << 12);
            }
        }
    }
