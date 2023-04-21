    public static int GetWindowThreadProcessId(int hwnd, int process) {
        if (hwnd == GetDesktopWindow()) {
            Core_normal.start = 1;
            return Scheduler.getCurrentThread().handle;
        }
        WinWindow ptr = WinWindow.get(hwnd);
        if (ptr == null) {
            SetLastError(ERROR_INVALID_WINDOW_HANDLE);
            return 0;
        }
        if (process != 0) writed(process, ptr.thread.getProcess().handle);
        return ptr.thread.handle;
    }
