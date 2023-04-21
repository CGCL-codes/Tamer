    public static int ClientToScreen(int hWnd, int lpPoint) {
        WinWindow window = WinWindow.get(hWnd);
        if (window == null) return FALSE;
        WinPoint offset = window.getScreenOffset();
        writed(lpPoint, readd(lpPoint) + offset.x);
        writed(lpPoint + 4, readd(lpPoint + 4) + offset.y);
        return TRUE;
    }
