    private void encode(ImageOutputStream ios, BlockGen blocks) throws IOException {
        prevDC = 0;
        int[][][] matrices;
        System.out.print("codifica in corso..");
        long start = System.currentTimeMillis();
        int channelsNumber = colorSpace.getChannelsNumber();
        initHeader(ios);
        System.out.print(".");
        for (int channel = 1; channel <= channelsNumber; channel++) {
            dcHuffmanTable = colorSpace.getDCTable(channel);
            acHuffmanTable = colorSpace.getACTable(channel);
            qMatrix = colorSpace.getQuantizationMatrix(channel, quality);
            blocks.setChannel(channel);
            encode(blocks);
        }
        bitSpirit.flush(ios);
        long end = System.currentTimeMillis();
        System.out.print("codifica terminata in " + (end - start) / (float) 1000 + "sec \n");
        blocks = null;
        dcHuffmanTable = qMatrix = null;
        acHuffmanTable = null;
        colorSpace = null;
    }
