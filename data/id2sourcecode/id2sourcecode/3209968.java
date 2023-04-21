    public int getIndex(int _iX, int _iY) {
        int iXn;
        int iYn;
        int iXf;
        int iYf;
        int iColumnNr;
        int iRowNr;
        iXn = (_iX - getX(0)) / (FIELD_WIDTH / 2);
        if (_iX < getX(0)) {
            iXn--;
        }
        iYn = (_iY - getY(0)) / (FIELD_HEIGHT / 2);
        if (_iY < getY(0)) {
            iYn--;
        }
        iXf = _iX - getX(0) - iXn * (FIELD_WIDTH / 2);
        iYf = _iY - getY(0) - iYn * (FIELD_HEIGHT / 2);
        if ((iXn + iYn) % 2 != 0) {
            if (2 * iYf < iXf) {
                iYn--;
            } else {
                iXn--;
            }
        } else {
            if (2 * iYf + iXf < (FIELD_WIDTH / 2)) {
                iXn--;
                iYn--;
            }
        }
        iColumnNr = (iXn + iYn) / 2;
        iRowNr = (iYn - iXn) / 2;
        if (((iColumnNr >= this.iRowLen) || (iRowNr >= this.iColumnLen)) || (iColumnNr < 0) || (iRowNr < 0)) {
            return -1;
        }
        return iColumnNr + iRowNr * this.iRowLen;
    }
