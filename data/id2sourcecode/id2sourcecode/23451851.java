    protected boolean generateContent(int nRows, int nCols, ActiveBagContent[] content, boolean useIds, ResourceBridge rb) {
        if (nRows <= 0 || nCols <= 0 || content == null || content.length < 1 || content[0] == null || rb == null) return false;
        Operacio o;
        Operacio[] op;
        int i, j, k;
        int[] tipus = new int[NTIPUSEX];
        int numTipus, tipX;
        boolean tipInv;
        String va, vb, vc, operator;
        String[] stra, strb, strc;
        int nColsB = nCols, nRowsB = nRows;
        int nCells = nRows * nCols;
        if (nCells < 2) return false;
        int[] ass = null;
        numTipus = 0;
        if (exp_abx) tipus[numTipus++] = ABX;
        if (exp_axc) tipus[numTipus++] = AXC;
        if (exp_xbc) tipus[numTipus++] = XBC;
        if (exp_axbc) tipus[numTipus++] = AXBC;
        if (numTipus == 0) return false;
        tipInv = exp_caxb;
        op = new Operacio[nCells];
        stra = new String[nCells];
        strb = new String[nCells];
        strc = new String[nCells];
        for (i = 0; i < nCells; i++) {
            o = new Operacio();
            for (j = 0; j < NMAXLOOPS; j++) {
                genOp(o);
                if (resultNoDup) {
                    for (k = 0; k < i; k++) {
                        if (o.numR.vf == op[k].numR.vf) break;
                    }
                    if (k == i) break;
                } else break;
            }
            op[i] = o;
        }
        if (resultOrder != 0) {
            for (i = nCells - 1; i > 0; i--) {
                for (j = 0; j < i; j++) {
                    if ((resultOrder == SORTASC && op[j].numR.vf > op[j + 1].numR.vf) || (resultOrder == SORTDESC && op[j].numR.vf < op[j + 1].numR.vf)) {
                        o = op[j];
                        op[j] = op[j + 1];
                        op[j + 1] = o;
                    }
                }
            }
        }
        for (i = 0; i < nCells; i++) {
            tipX = tipus[random.nextInt(numTipus)];
            va = getDF(op[0].numA.c).format(op[i].numA.vf);
            vb = getDF(op[0].numB.c).format(op[i].numB.vf);
            vc = getDF(op[0].numR.c).format(op[i].numR.vf);
            operator = OPSTR[op[i].op];
            if (tipInv) strc[i] = new String(vc + S + "=" + S + va + S + operator + S + vb); else strc[i] = new String(va + S + operator + S + vb + S + "=" + S + vc);
            switch(tipX) {
                case AXC:
                    strb[i] = new String(vb);
                    if (tipInv) stra[i] = new String(vc + S + "=" + S + va + S + operator + S + "?"); else stra[i] = new String(va + S + operator + S + "?" + S + "=" + S + vc);
                    break;
                case XBC:
                    strb[i] = new String(va);
                    if (tipInv) stra[i] = new String(vc + S + "=" + S + "?" + S + operator + S + vb); else stra[i] = new String("?" + S + operator + S + vb + S + "=" + S + vc);
                    break;
                case AXBC:
                    strb[i] = new String(operator);
                    if (tipInv) stra[i] = new String(vc + S + "=" + S + va + S + "?" + S + vb); else stra[i] = new String(va + S + "?" + S + vb + S + "=" + S + vc);
                    break;
                default:
                    strb[i] = new String(vc);
                    if (tipInv) stra[i] = new String("?" + S + "=" + S + va + S + operator + S + vb); else stra[i] = new String(va + S + operator + S + vb + S + "=");
                    break;
            }
        }
        if (useIds) {
            ass = new int[nCells];
            String[] strbx = new String[nCells];
            k = 0;
            for (i = 0; i < nCells; i++) {
                for (j = 0; j < k; j++) if (strb[i].equals(strbx[j])) break;
                if (j == k) {
                    strbx[k] = strb[i];
                    ass[i] = k;
                    k++;
                } else ass[i] = j;
            }
            strb = new String[k];
            for (i = 0; i < k; i++) strb[i] = strbx[i];
            if (nRowsB * nColsB != k) {
                boolean distH = false;
                switch(k) {
                    case 6:
                        nRowsB = distH ? 2 : 3;
                        nColsB = distH ? 3 : 2;
                        break;
                    case 8:
                        nRowsB = distH ? 2 : 4;
                        nColsB = distH ? 4 : 2;
                        break;
                    case 9:
                        nRowsB = 3;
                        nColsB = 3;
                        break;
                    case 10:
                        nRowsB = distH ? 2 : 5;
                        nColsB = distH ? 5 : 2;
                        break;
                    case 12:
                        nRowsB = distH ? 3 : 4;
                        nColsB = distH ? 4 : 3;
                        break;
                    case 14:
                        nRowsB = distH ? 2 : 7;
                        nColsB = distH ? 7 : 2;
                        break;
                    case 15:
                        nRowsB = distH ? 3 : 5;
                        nColsB = distH ? 3 : 5;
                        break;
                    case 16:
                        nRowsB = 4;
                        nColsB = 4;
                        break;
                    case 18:
                        nRowsB = distH ? 6 : 3;
                        nColsB = distH ? 3 : 6;
                        break;
                    case 20:
                        nRowsB = distH ? 4 : 5;
                        nColsB = distH ? 5 : 4;
                        break;
                    default:
                        nRowsB = distH ? 1 : k;
                        nColsB = distH ? k : 1;
                        break;
                }
            }
        }
        content[0].setTextContent(stra, nCols, nRows);
        if (ass != null) content[0].setIds(ass);
        if (content.length > 1 && content[1] != null) {
            content[1].setTextContent(strb, nColsB, nRowsB);
            content[1].getShaper().reset(nColsB, nRowsB);
        }
        if (content.length > 2 && content[2] != null) content[2].setTextContent(strc, nCols, nRows);
        return true;
    }
