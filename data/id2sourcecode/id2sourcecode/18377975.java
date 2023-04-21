    public static void format() {
        for (int i = 2; i < 16; i++) {
            loginCard((byte) i);
            System.out.println("----------------------sector    :   " + "\t" + i + "-----------------------------------------------------");
            byte[] readData = new byte[16];
            for (int j = i * 4; j < i * 4 + 4; j++) {
                acr120u.read(hReader, (byte) j, readData);
                System.out.println("block   " + j + "\t" + new String(readData));
                if (j != i * 4 + 3) {
                    for (int k = 0; k < readData.length; k++) {
                        readData[k] = (byte) 0;
                    }
                    acr120u.write(hReader, (byte) j, readData);
                }
            }
        }
    }
