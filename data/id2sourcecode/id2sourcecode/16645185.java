    private String expectedPairingCode(String code) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            os.write(pairCode.getBytes("UTF-8"));
            byte passcode[] = code.getBytes("UTF-8");
            for (int c = 0; c < passcode.length; c++) {
                os.write(passcode[c]);
                os.write(0);
            }
            return toHex(md5.digest(os.toByteArray()));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            return "";
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return "";
        }
    }
