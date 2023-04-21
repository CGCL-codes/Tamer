    @Test
    public void testTessDllRecognize_a_Block() throws Exception {
        System.out.println("TessDllRecognize_a_Block");
        String expResult = "The (quick) [brown] {fox} jumps!\nOver the $43,456.78 <lazy> #90 dog";
        String lang = "eng";
        File tiff = new File("eurotext.tif");
        BufferedImage image = ImageIO.read(tiff);
        MappedByteBuffer buf = new FileInputStream(tiff).getChannel().map(MapMode.READ_ONLY, 0, tiff.length());
        int resultRead = TessDllAPI1.TessDllBeginPageUpright(image.getWidth(), image.getHeight(), buf, lang);
        ETEXT_DESC output = TessDllAPI1.TessDllRecognize_a_Block(91, 91 + 832, 170, 170 + 614);
        EANYCODE_CHAR[] text = (EANYCODE_CHAR[]) output.text[0].toArray(output.count);
        List<Byte> unistr = new ArrayList<Byte>();
        int j = 0;
        for (int i = 0; i < output.count; i = j) {
            final EANYCODE_CHAR ch = text[i];
            for (int b = 0; b < ch.blanks; ++b) {
                unistr.add((byte) ' ');
            }
            for (j = i; j < output.count; j++) {
                final EANYCODE_CHAR unich = text[j];
                if (ch.left != unich.left || ch.right != unich.right || ch.top != unich.top || ch.bottom != unich.bottom) {
                    break;
                }
                unistr.add(unich.char_code);
            }
            if ((ch.formatting & 64) == 64) {
                unistr.add((byte) '\n');
            } else if ((ch.formatting & 128) == 128) {
                unistr.add((byte) '\n');
                unistr.add((byte) '\n');
            }
        }
        byte[] bb = Tesseract.wrapperListToByteArray(unistr);
        String result = new String(bb, "utf8");
        System.out.println(result);
        assertEquals(expResult, result.substring(0, expResult.length()));
    }
