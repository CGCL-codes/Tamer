    public static boolean clearBlock(int blockIndex) {
        int sectorIndex = blockIndex / 4;
        System.out.println("index  :  " + sectorIndex);
        loginCard((byte) sectorIndex);
        byte[] readData = new byte[16];
        acr120u.read(hReader, (byte) blockIndex, readData);
        if (blockIndex != sectorIndex * 4 + 3) {
            for (int i = 0; i < readData.length; i++) {
                readData[i] = (byte) 0x00;
            }
            acr120u.write(hReader, (byte) blockIndex, readData);
            return true;
        } else {
            System.out.println("khong the xoa du lieu trong block nay.");
            return false;
        }
    }
