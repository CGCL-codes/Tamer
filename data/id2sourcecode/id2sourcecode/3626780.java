        public void onCall() {
            int This = CPU.CPU_Pop32();
            int lpDDSCaps = CPU.CPU_Pop32();
            if (lpDDSCaps == 0) {
                CPU_Regs.reg_eax.dword = Error.E_POINTER;
            } else {
                Memory.mem_writed(lpDDSCaps, Memory.mem_readd(This + OFFSET_DATA_START + OFFSET_DESC + 0x68));
                if (isCap2(This)) {
                    Memory.mem_writed(lpDDSCaps + 4, Memory.mem_readd(This + OFFSET_DATA_START + OFFSET_DESC + 0x68 + 4));
                    Memory.mem_writed(lpDDSCaps + 8, Memory.mem_readd(This + OFFSET_DATA_START + OFFSET_DESC + 0x68 + 8));
                    Memory.mem_writed(lpDDSCaps + 12, Memory.mem_readd(This + OFFSET_DATA_START + OFFSET_DESC + 0x68 + 12));
                }
                CPU_Regs.reg_eax.dword = Error.S_OK;
            }
        }
