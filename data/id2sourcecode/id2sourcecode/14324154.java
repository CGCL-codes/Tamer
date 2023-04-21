    public ICCColorSpace(PdfObjectReader currentPdfFile, String currentColorspace) {
        a1 = new int[256];
        b1 = new int[256];
        c1 = new int[256];
        for (int i = 0; i < 256; i++) {
            a1[i] = -1;
            b1[i] = -1;
            c1[i] = -1;
        }
        value = ColorSpaces.ICC;
        cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        int pointer = currentColorspace.indexOf("/ICCBased");
        String object_ref = currentColorspace.substring(pointer + 9);
        pointer = object_ref.indexOf("]");
        if (pointer != -1) object_ref = object_ref.substring(0, pointer - 1);
        byte[] icc_data = currentPdfFile.readStream(object_ref.trim(), true);
        if (icc_data == null) LogWriter.writeLog("Error in reading ICC data with ref " + object_ref); else {
            try {
                cs = new ICC_ColorSpace(ICC_Profile.getInstance(icc_data));
            } catch (Exception e) {
                LogWriter.writeLog("[PDF] Problem " + e.getMessage() + " with ICC data ");
                failed = true;
            }
        }
        componentCount = cs.getNumComponents();
    }
