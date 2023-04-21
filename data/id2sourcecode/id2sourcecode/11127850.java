    public static void main(String[] args) {
        LB = Log_Buffer.getLogBufferInstance();
        LB.addPrintStream(System.out);
        Thread th = new Thread(LB);
        th.start();
        new CurrentVersion(LB);
        LB.Version("Two way Venn diagram generator", "$Revision: 2740 $");
        HashMap<String, String> Variables = null;
        try {
            Variables = CommandLine.process_CLI(args);
        } catch (CommandLineProcessingException CLPE) {
            LB.error(CLPE.getMessage());
            LB.die();
        }
        parse_input(Variables);
        assert (Variables != null);
        if (!output_file.endsWith(".svg")) {
            output_file = output_file.concat(".svg");
        }
        int AREA1 = 50000;
        int max = Math.max(value1, value2);
        double r1 = Math.sqrt((double) AREA1 / Math.PI);
        int min = Math.min(value1, value2);
        double r2 = Math.sqrt(((double) AREA1 * min) / (max * Math.PI));
        double overlap_pixels = (double) AREA1 * overlap / max;
        double maxd = r1 + r2;
        double mind = r1 - r2;
        double d = r1;
        for (int x = 0; x < iterations; x++) {
            double area = area_of_overlap(r1, r2, d);
            if (area < overlap_pixels) {
                maxd = d;
            } else if (area > overlap_pixels) {
                mind = d;
            } else {
                continue;
            }
            d = (mind + maxd) / 2;
        }
        double margin = (WIDTH - (r1 + r2 + d)) / 2;
        SVGWriter s1 = new SVGWriter(LB, output_file);
        s1.create_header(WIDTH, HEIGHT);
        s1.add_defs();
        s1.BlendGroupOn();
        s1.circle((float) (margin + r1), (float) MID_HEIGHT, (float) r1, RGB1[0], RGB1[1], RGB1[2], 4);
        s1.circle((float) (margin + r1 + d), (float) MID_HEIGHT, (float) r2, RGB2[0], RGB2[1], RGB2[2], 4);
        s1.BlendGroupOff();
        s1.AnchorText(SVGWriter.ANCHORLEFT);
        s1.text((int) (margin + PADDING), MID_HEIGHT, FONT_SIZE, 0, 0, 0, ((value1 == max) ? String.valueOf(value1 - overlap) : String.valueOf(value2 - overlap)));
        s1.AnchorText(SVGWriter.ANCHORRIGHT);
        s1.text((int) (WIDTH - (margin + PADDING)), MID_HEIGHT, FONT_SIZE, 0, 0, 0, ((value2 == min) ? String.valueOf(value2 - overlap) : String.valueOf(value1 - overlap)));
        s1.AnchorText(SVGWriter.ANCHORCENTER);
        s1.text((int) (margin + (2 * r1) - ((r1 + r2 - d) / 2)), MID_HEIGHT, FONT_SIZE, 0, 0, 0, String.valueOf(overlap));
        if (name1 != null && name2 != null) {
            s1.AnchorText(SVGWriter.ANCHORRIGHT);
            s1.text((int) (margin - 5 + (r1 - (Math.cos(Math.PI / 4) * r1))), (int) (MID_HEIGHT - r1 * Math.sin(Math.PI / 4)), FONT_SIZE, 0, 0, 0, ((value1 == max) ? name1 + " - " + value1 : name2 + " - " + value2));
            s1.AnchorText(SVGWriter.ANCHORLEFT);
            s1.text((int) (WIDTH - margin - (r2 - (Math.cos(Math.PI / 4) * r2)) + 5), (int) (MID_HEIGHT - r2 * Math.sin(Math.PI / 4)), FONT_SIZE, 0, 0, 0, ((value2 == min) ? name2 + " - " + value2 : name1 + " - " + value1));
        }
        s1.close();
        LB.close();
    }
