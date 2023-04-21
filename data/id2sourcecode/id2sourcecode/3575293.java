    public BufferedImage createScreenCapture(Rectangle screenRect) {
        int w = screenRect.width;
        int h = screenRect.height;
        int size = w * h;
        long compBMP = createScreenBMP(screenRect);
        if (compBMP == 0l) {
            return null;
        }
        Win32.BITMAPINFO bmi = createAndFillBMI(w, h);
        Int32Pointer pData = nb.createInt32Pointer(size, false);
        win32.DeleteObject(compBMP);
        long screenDC = win32.GetDC(0l);
        int nLines = win32.GetDIBits(screenDC, compBMP, 0, h, pData, bmi, WindowsDefs.DIB_RGB_COLORS);
        win32.ReleaseDC(0, screenDC);
        if (nLines != h) {
            return null;
        }
        BufferedImage bufImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int[] data = new int[size];
        pData.get(data, 0, data.length);
        bufImg.setRGB(0, 0, w, h, data, 0, w);
        return bufImg;
    }
