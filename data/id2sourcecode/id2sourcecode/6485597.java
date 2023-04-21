    private String getChannelFlags(int flag) {
        String clflag = null;
        if (flag == 30) {
            clflag = "(RMPSD)";
        } else if (flag == 28) {
            clflag = "(RPSD)";
        } else if (flag == 26) {
            clflag = "(RMSD)";
        } else if (flag == 24) {
            clflag = "(RSD)";
        } else if (flag == 22) {
            clflag = "(RMPD)";
        } else if (flag == 20) {
            clflag = "(RPD)";
        } else if (flag == 18) {
            clflag = "(RMD)";
        } else if (flag == 16) {
            clflag = "(RD)";
        } else if (flag == 15) {
            clflag = "(UMPS)";
        } else if (flag == 14) {
            clflag = "(RMPS)";
        } else if (flag == 13) {
            clflag = "(UPS)";
        } else if (flag == 12) {
            clflag = "(RPS)";
        } else if (flag == 11) {
            clflag = "(UMS)";
        } else if (flag == 10) {
            clflag = "(RMS)";
        } else if (flag == 9) {
            clflag = "(US)";
        } else if (flag == 8) {
            clflag = "(RS)";
        } else if (flag == 7) {
            clflag = "(UMP)";
        } else if (flag == 6) {
            clflag = "(RMP)";
        } else if (flag == 5) {
            clflag = "(UP)";
        } else if (flag == 4) {
            clflag = "(RP)";
        } else if (flag == 3) {
            clflag = "(UM)";
        } else if (flag == 2) {
            clflag = "(RM)";
        } else if (flag == 1) {
            clflag = "(U)";
        } else if (flag == 0) {
            clflag = "(R)";
        } else {
            clflag = "";
        }
        return clflag;
    }
