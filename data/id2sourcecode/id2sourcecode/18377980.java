    public static boolean clearSector(int sectorIndex) {
        loginCard((byte) sectorIndex);
        for (int i = sectorIndex * 4; i < sectorIndex * 4 + 4; i++) {
            byte[] readData = new byte[16];
            if (i != sectorIndex * 4 + 3) {
                acr120u.read(hReader, (byte) i, readData);
                for (int j = 0; j < readData.length; j++) {
                    readData[j] = (byte) 0x00;
                }
                acr120u.write(hReader, (byte) i, readData);
                return true;
            } else {
                System.out.println("khong the xoa du lieu trong block nay.");
            }
        }
        return false;
    }
