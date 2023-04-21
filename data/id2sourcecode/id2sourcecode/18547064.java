    private static void handleIpxRequest() {
        ECBClass tmpECB;
        switch(CPU_Regs.reg_ebx.word()) {
            case 0x0000:
                OpenSocket();
                Log.log_msg(StringHelper.sprintf("IPX: Open socket %4x", new Object[] { new Integer(swapByte(CPU_Regs.reg_edx.word())) }));
                break;
            case 0x0001:
                Log.log_msg(StringHelper.sprintf("IPX: Close socket %4x", new Object[] { new Integer(swapByte(CPU_Regs.reg_edx.word())) }));
                CloseSocket();
                break;
            case 0x0002:
                for (int i = 0; i < 6; i++) Memory.real_writeb((int) CPU.Segs_ESval, CPU_Regs.reg_edi.word() + i, Memory.real_readb((int) CPU.Segs_ESval, CPU_Regs.reg_esi.word() + i + 4));
                CPU_Regs.reg_ecx.word(1);
                CPU_Regs.reg_eax.low(0x00);
                break;
            case 0x0003:
                tmpECB = new ECBClass((int) CPU.Segs_ESval, CPU_Regs.reg_esi.word());
                if (!incomingPacket.connected) {
                    tmpECB.setInUseFlag(USEFLAG_AVAILABLE);
                    tmpECB.setCompletionFlag(COMP_UNDELIVERABLE);
                    tmpECB.close();
                    CPU_Regs.reg_eax.low(0xff);
                } else {
                    tmpECB.setInUseFlag(USEFLAG_SENDING);
                    CPU_Regs.reg_eax.low(0x00);
                    sendPacket(tmpECB);
                }
                break;
            case 0x0004:
                tmpECB = new ECBClass((int) CPU.Segs_ESval, CPU_Regs.reg_esi.word());
                if (!sockInUse(tmpECB.getSocket())) {
                    CPU_Regs.reg_eax.low(0xff);
                    tmpECB.setInUseFlag(USEFLAG_AVAILABLE);
                    tmpECB.setCompletionFlag(COMP_HARDWAREERROR);
                    tmpECB.close();
                } else {
                    CPU_Regs.reg_eax.low(0x00);
                    tmpECB.setInUseFlag(USEFLAG_LISTENING);
                }
                break;
            case 0x0005:
            case 0x0007:
                {
                    tmpECB = new ECBClass((int) CPU.Segs_ESval, CPU_Regs.reg_esi.word());
                    Pic.PIC_AddEvent(IPX_AES_EventHandler, (1000.0f / (1193182.0f / 65536.0f)) * (float) CPU_Regs.reg_eax.word(), (int) tmpECB.ECBAddr);
                    tmpECB.setInUseFlag(USEFLAG_AESCOUNT);
                    break;
                }
            case 0x0006:
                {
                    int ecbaddress = Memory.RealMake((int) CPU.Segs_ESval, CPU_Regs.reg_esi.word());
                    tmpECB = ECBList;
                    ECBClass tmp2ECB;
                    while (tmpECB != null) {
                        tmp2ECB = tmpECB.nextECB;
                        if (tmpECB.ECBAddr == ecbaddress) {
                            if (tmpECB.getInUseFlag() == USEFLAG_AESCOUNT) Pic.PIC_RemoveSpecificEvents(IPX_AES_EventHandler, (int) ecbaddress);
                            tmpECB.setInUseFlag(USEFLAG_AVAILABLE);
                            tmpECB.setCompletionFlag(COMP_CANCELLED);
                            tmpECB.close();
                            CPU_Regs.reg_eax.low(0);
                            Log.log_msg("IPX: ECB canceled.");
                            return;
                        }
                        tmpECB = tmp2ECB;
                    }
                    CPU_Regs.reg_eax.low(0xff);
                    break;
                }
            case 0x0008:
                CPU_Regs.reg_eax.word(Memory.mem_readw(0x46c));
                break;
            case 0x0009:
                {
                    Log.log_msg(StringHelper.sprintf("IPX: Get internetwork address %2x:%2x:%2x:%2x:%2x:%2x", new Object[] { new Integer(localIpxAddr.netnode[5] & 0xFF), new Integer(localIpxAddr.netnode[4] & 0xFF), new Integer(localIpxAddr.netnode[3] & 0xFF), new Integer(localIpxAddr.netnode[2] & 0xFF), new Integer(localIpxAddr.netnode[1] & 0xFF), new Integer(localIpxAddr.netnode[0] & 0xFF) }));
                    byte[] addrptr = localIpxAddr.toByteArray();
                    for (int i = 0; i < 10; i++) Memory.real_writeb((int) CPU.Segs_ESval, CPU_Regs.reg_esi.word() + i, addrptr[i]);
                    break;
                }
            case 0x000a:
                break;
            case 0x000b:
                break;
            case 0x000d:
                CPU_Regs.reg_ecx.word(0);
                CPU_Regs.reg_eax.word(1024);
                break;
            case 0x0010:
                CPU_Regs.reg_eax.low(0);
                break;
            case 0x001a:
                CPU_Regs.reg_ecx.word(0);
                CPU_Regs.reg_eax.word(IPXBUFFERSIZE);
                break;
            default:
                Log.log_msg(StringHelper.sprintf("Unhandled IPX function: %4x", new Object[] { new Integer(CPU_Regs.reg_ebx.word()) }));
                break;
        }
    }
