    private void dispLockBits(int locks) {
        String s;
        mf.showMsgLn("Lock bits: [" + Utils.byteToHex(locks) + "]", HEADER_CL);
        switch(locks & 3) {
            case 3:
                s = "No memory lock features enabled";
                break;
            case 2:
                s = "Further programming of the Flash and EEPROM is disabled in Parallel and SPI/JTAG Serial Programming mode. The Fuse bits are locked in both Serial and Parallel Programming mode";
                break;
            case 1:
                s = "[Invalid lock bits combination]";
                break;
            case 0:
                s = "Further programming and verification of the Flash and EEPROM is disabled in Parallel and SPI/JTAG Serial Programming mode. The Fuse bits are locked in both Serial and Parallel Programming mode";
                break;
            default:
                s = "[Internal error]";
        }
        mf.showMsgLn(">" + s, ITEM_CL);
        switch((locks >>> 2) & 3) {
            case 3:
                s = "No restrictions for SPM or LPM accessing the Application section";
                break;
            case 2:
                s = "SPM is not allowed to write to the Application section";
                break;
            case 1:
                s = "LPM executing from the Boot Loader section is not allowed to read from the Application section. If interrupt vectors are placed in the Boot Loader section, interrupts are disabled while executing from the Application section";
                break;
            case 0:
                s = "SPM is not allowed to write to the Application section, and LPM executing from the Boot Loader section is not allowed to read from the Application section. If interrupt vectors are placed in the Boot Loader section, interrupts are disabled while executing from the Application section";
                break;
            default:
                s = "[Internal error]";
        }
        mf.showMsgLn(">" + s, ITEM_CL);
        switch((locks >>> 4) & 3) {
            case 3:
                s = "No restrictions for SPM or LPM accessing the Boot Loader section";
                break;
            case 2:
                s = "SPM is not allowed to write to the Boot Loader section";
                break;
            case 1:
                s = "LPM executing from the Application section is not allowed to read from the Boot Loader section. If interrupt vectors are placed in the Application section, interrupts are disabled while executing from the Boot Loader section";
                break;
            case 0:
                s = "SPM is not allowed to write to the Boot Loader section, and LPM executing from the Application section is not allowed to read from the Boot Loader section. If interrupt vectors are placed in the Application section, interrupts are disabled while executing from the Boot Loader section";
                break;
            default:
                s = "[Internal error]";
        }
        mf.showMsgLn(">" + s, ITEM_CL);
    }
