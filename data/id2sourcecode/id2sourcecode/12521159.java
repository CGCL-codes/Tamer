    @Test
    public void testTessDllRecognize_all_Words() throws Exception {
        System.out.println("TessDllRecognize_all_Words");
        TessDllAPI api = new TessDllAPIImpl().getInstance();
        String expResult = "The (quick) [brown] {fox} jumps!\nOver the $43,456.78 <lazy> #90 dog";
        String lang = "eng";
        File tiff = new File("eurotext.tif");
        BufferedImage image = ImageIO.read(new FileInputStream(tiff));
        MappedByteBuffer buf = new FileInputStream(tiff).getChannel().map(MapMode.READ_ONLY, 0, tiff.length());
        int resultRead = api.TessDllBeginPageUpright(image.getWidth(), image.getHeight(), buf, lang);
        ETEXT_DESC output = api.TessDllRecognize_all_Words();
        EANYCODE_CHAR[] text = (EANYCODE_CHAR[]) output.text[0].toArray(output.count);
        StringBuilder sb = new StringBuilder();
        int j = 0;
        for (int i = 0; i < output.count; i = j) {
            final EANYCODE_CHAR ch = text[i];
            List<Byte> unistr = new ArrayList<Byte>();
            for (int b = 0; b < ch.blanks; ++b) {
                sb.append(" ");
            }
            for (j = i; j < output.count; j++) {
                final EANYCODE_CHAR unich = text[j];
                if (ch.left != unich.left || ch.right != unich.right || ch.top != unich.top || ch.bottom != unich.bottom) {
                    break;
                }
                unistr.add(unich.char_code);
            }
            byte[] bb = Tesseract.wrapperListToByteArray(unistr);
            String chr = new String(bb, "utf8");
            sb.append(chr);
            if ((ch.formatting & 64) == 64) {
                sb.append('\n');
            } else if ((ch.formatting & 128) == 128) {
                sb.append("\n\n");
            }
        }
        String result = sb.toString();
        System.out.println(result);
        assertEquals(expResult, result.substring(0, expResult.length()));
    }
