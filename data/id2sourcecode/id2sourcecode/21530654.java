    public static void packItemPart2() {
        try {
            for (int i = 0; i < Cache.getAmountOfItems(); i++) {
                final File item = new File("./Data/Items/bonus/" + i + ".txt");
                if (!item.exists()) continue; else {
                    BufferedReader in = new BufferedReader(new FileReader(item));
                    Cache.getItemDefinitionFile2().seek(31 * i);
                    for (int i2 = 0; i2 < 15; i2++) {
                        Cache.getItemDefinitionFile2().writeShort(Integer.parseInt(in.readLine()));
                    }
                    Cache.getItemDefinitionFile2().writeByte(Integer.parseInt(in.readLine()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
