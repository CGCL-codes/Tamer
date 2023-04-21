    private static void CloseSocket() {
        int sockNum, i;
        ECBClass tmpECB = ECBList;
        ECBClass tmp2ECB = ECBList;
        sockNum = swapByte(CPU_Regs.reg_edx.word());
        if (!sockInUse(sockNum)) return;
        for (i = 0; i < socketCount - 1; i++) {
            if (opensockets[i] == sockNum) {
                for (int j = i; j < SOCKETTABLESIZE - 1; j++) opensockets[j] = opensockets[j + 1];
                break;
            }
        }
        --socketCount;
        while (tmpECB != null) {
            tmp2ECB = tmpECB.nextECB;
            if (tmpECB.getSocket() == sockNum) {
                tmpECB.setCompletionFlag(COMP_CANCELLED);
                tmpECB.setInUseFlag(USEFLAG_AVAILABLE);
                tmpECB.close();
            }
            tmpECB = tmp2ECB;
        }
    }
