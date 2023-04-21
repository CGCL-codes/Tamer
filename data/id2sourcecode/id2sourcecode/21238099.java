    public void calcLightParameters() {
        int ldHigh;
        int ldLow;
        ldHigh = lLMax - lRMin;
        ldLow = lLMin - lRMax;
        ldRange = (ldHigh - ldLow) / 2;
        ldMid = (ldHigh + ldLow) / 2;
        lLCrossThresh = (lLMin * 2 + lLCross) / 3;
        lRCrossThresh = (lRMin * 2 + lRCross) / 3;
        int mult = 4;
        lLLowThresh = (lLMin * mult + lLMax) / (mult + 1);
        lRLowThresh = (lRMin * mult + lRMax) / (mult + 1);
        lMLowThresh = (lMMin * mult + lMMax) / (mult + 1);
        lLHighThresh = (lLMax * mult + lLMin) / (mult + 1);
        lRHighThresh = (lRMax * mult + lRMin) / (mult + 1);
        lMHighThresh = (lMMax * mult + lMMin) / (mult + 1);
        lALowThresh = ((lLMin + lRMin + lMMin) * mult + (lLMax + lRMax + lMMax)) / (mult + 1);
        lAHighThresh = ((lLMax + lRMax + lMMax) * mult + (lLMin + lRMin + lMMin)) / (mult + 1);
    }
