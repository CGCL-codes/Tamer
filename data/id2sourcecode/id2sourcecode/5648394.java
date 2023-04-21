    public static int GetKeyboardState(int lpKeyState) {
        for (int i = 0; i < 256; i++) {
            if (Scheduler.getCurrentThread().getKeyState().get(i)) Memory.mem_writeb(lpKeyState + i, 0x80); else Memory.mem_writeb(lpKeyState + i, 0x0);
        }
        return WinAPI.TRUE;
    }
