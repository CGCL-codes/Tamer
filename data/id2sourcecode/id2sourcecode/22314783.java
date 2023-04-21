    void write(byte buf[], int off, int read) throws IOException {
        int i;
        for (i = off; i < read; ) {
            if (telnetState == 0) {
                if (buf[i] == (byte) 0xFF) {
                    telnetState = 1;
                    i++;
                } else process(buf[i++]);
            } else if (telnetState == 1) {
                switch(buf[i]) {
                    case (byte) 240:
                        telnetState = 0;
                        i++;
                        break;
                    case (byte) 241:
                        telnetState = 0;
                        i++;
                        break;
                    case (byte) 242:
                        telnetState = 0;
                        i++;
                        break;
                    case (byte) 243:
                        telnetState = 0;
                        i++;
                        break;
                    case (byte) 244:
                        telnetState = 0;
                        i++;
                        break;
                    case (byte) 245:
                        telnetState = 0;
                        i++;
                        break;
                    case (byte) 246:
                        telnetState = 0;
                        i++;
                        break;
                    case (byte) 247:
                        telnetState = 0;
                        i++;
                        break;
                    case (byte) 248:
                        telnetState = 0;
                        i++;
                        break;
                    case (byte) 249:
                        telnetState = 0;
                        i++;
                        break;
                    case (byte) 250:
                        telnetState = 10;
                        i++;
                        break;
                    case (byte) 251:
                        telnetState = 11;
                        i++;
                        break;
                    case (byte) 252:
                        telnetState = 12;
                        i++;
                        break;
                    case (byte) 253:
                        telnetState = 13;
                        i++;
                        break;
                    case (byte) 254:
                        telnetState = 14;
                        i++;
                        break;
                    case (byte) 255:
                        telnetState = 0;
                        process(buf[i++]);
                        break;
                    default:
                        telnetState = 0;
                        i++;
                }
            } else switch(telnetState) {
                case 10:
                    telnetState = 0;
                    i++;
                    break;
                case 11:
                    telnetState = 0;
                    if (option[buf[i]] == false) {
                        sendcmd(DONT, buf[i]);
                        option[buf[i]] = true;
                    }
                    i++;
                    break;
                case 12:
                    telnetState = 0;
                    i++;
                    break;
                case 13:
                    telnetState = 0;
                    if (option[buf[i]] == false) {
                        sendcmd(WONT, buf[i]);
                        option[buf[i]] = true;
                    }
                    i++;
                    break;
                case 14:
                    telnetState = 0;
                    i++;
                    break;
            }
        }
        repaint();
    }
