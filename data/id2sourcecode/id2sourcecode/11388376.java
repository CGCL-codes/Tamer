    static void TEXT_Ellipsify(int hdc, int str, int max_len, IntRef len_str, int width, WinSize size, int modstr, IntRef len_before, IntRef len_ellip) {
        int len_ellipsis;
        int lo, mid, hi;
        len_ellipsis = ELLIPSIS.length();
        if (len_ellipsis > max_len) len_ellipsis = max_len;
        if (len_str.value > max_len - len_ellipsis) len_str.value = max_len - len_ellipsis;
        int pSize = getTempBuffer(WinSize.SIZE);
        if (len_str.value > 0 && WinFont.GetTextExtentExPointA(hdc, str, len_str.value, width, NULL, NULL, pSize) != 0) {
            size.copy(pSize);
            if (size.cx > width) {
                for (lo = 0, hi = len_str.value; lo < hi; ) {
                    mid = (lo + hi) / 2;
                    if (WinFont.GetTextExtentExPointA(hdc, str, mid, width, NULL, NULL, pSize) == 0) break;
                    size.copy(pSize);
                    if (size.cx > width) hi = mid; else lo = mid + 1;
                }
                len_str.value = hi;
            }
        }
        while (true) {
            Memory.mem_memcpy(str + len_str.value, ELLIPSIS.getBytes(), 0, len_ellipsis);
            if (WinFont.GetTextExtentExPointA(hdc, str, len_str.value + len_ellipsis, width, NULL, NULL, pSize) == 0) break;
            if (len_str.value == 0 || size.cx <= width) break;
            len_str.value--;
        }
        len_ellip.value = len_ellipsis;
        len_before.value = len_str.value;
        len_str.value += len_ellipsis;
        if (modstr != 0) {
            Memory.mem_memcpy(modstr, str, len_str.value);
            writeb(modstr + len_str.value, 0);
        }
    }
