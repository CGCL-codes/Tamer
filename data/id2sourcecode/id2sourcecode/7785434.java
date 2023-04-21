    private void swapaux() {
        index = 2;
        byte i;
        i = event[index];
        sevent[index] = event[index + 1];
        sevent[index + 1] = i;
        index += 2;
        i = (byte) ((event[0] & ~0x80) & 0xff);
        switch(i) {
            case 0:
                _swapInt();
                _swapShort();
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                i = event[index];
                sevent[index] = event[index + 3];
                sevent[index + 3] = i;
                i = event[index + 1];
                sevent[index + 1] = event[index + 2];
                sevent[index + 2] = i;
                index += 4;
                i = event[index];
                sevent[index] = event[index + 3];
                sevent[index + 3] = i;
                i = event[index + 1];
                sevent[index + 1] = event[index + 2];
                sevent[index + 2] = i;
                index += 4;
                i = event[index];
                sevent[index] = event[index + 3];
                sevent[index + 3] = i;
                i = event[index + 1];
                sevent[index + 1] = event[index + 2];
                sevent[index + 2] = i;
                index += 4;
                i = event[index];
                sevent[index] = event[index + 3];
                sevent[index + 3] = i;
                i = event[index + 1];
                sevent[index + 1] = event[index + 2];
                sevent[index + 2] = i;
                index += 4;
                i = event[index];
                sevent[index] = event[index + 1];
                sevent[index + 1] = i;
                index += 2;
                i = event[index];
                sevent[index] = event[index + 1];
                sevent[index + 1] = i;
                index += 2;
                i = event[index];
                sevent[index] = event[index + 1];
                sevent[index + 1] = i;
                index += 2;
                i = event[index];
                sevent[index] = event[index + 1];
                sevent[index + 1] = i;
                index += 2;
                i = event[index];
                sevent[index] = event[index + 1];
                sevent[index + 1] = i;
                break;
            case 9:
            case 10:
                _swapInt();
                break;
            case 12:
                _swapInt();
                _swapShort();
                _swapShort();
                _swapShort();
                _swapShort();
                _swapShort();
                break;
            case 13:
                _swapInt();
                _swapShort();
                _swapShort();
                _swapShort();
                _swapShort();
                _swapShort();
                _swapShort();
                break;
            case 14:
                _swapInt();
                _swapShort();
                break;
            case 15:
                _swapInt();
                break;
            case 16:
                _swapInt();
                _swapInt();
                _swapShort();
                _swapShort();
                _swapShort();
                _swapShort();
                _swapShort();
                break;
            case 17:
            case 18:
            case 19:
            case 20:
                _swapInt();
                _swapInt();
                break;
            case 21:
                _swapInt();
                _swapInt();
                _swapInt();
                _swapShort();
                _swapShort();
                break;
            case 22:
                _swapInt();
                _swapInt();
                _swapInt();
                _swapShort();
                _swapShort();
                _swapShort();
                _swapShort();
                _swapShort();
                break;
            case 23:
                _swapInt();
                _swapInt();
                _swapInt();
                _swapShort();
                _swapShort();
                _swapShort();
                _swapShort();
                _swapShort();
                _swapShort();
                break;
            case 24:
                _swapInt();
                _swapInt();
                _swapShort();
                _swapShort();
                break;
            case 25:
                _swapInt();
                _swapShort();
                _swapShort();
                break;
            case 26:
            case 27:
            case 28:
            case 29:
                _swapInt();
                _swapInt();
                _swapInt();
                break;
            case 30:
                _swapInt();
                _swapInt();
                _swapInt();
                _swapInt();
                _swapInt();
                _swapInt();
                break;
            case 31:
                _swapInt();
                _swapInt();
                _swapInt();
                _swapInt();
                _swapInt();
                break;
            case 32:
                _swapInt();
                _swapInt();
                break;
            case 33:
                _swapInt();
                _swapInt();
                switch(event[1]) {
                    case 32:
                        for (int ii = 0; ii < 5; ii++) _swapInt();
                        break;
                    default:
                }
                break;
            default:
                if (64 < i) {
                    Extension.swap((int) i, this);
                }
        }
    }
