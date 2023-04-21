    private void createPage1(PdfContentByte canvas) throws BadElementException, MalformedURLException, IOException, DocumentException {
        URL imageUrl = getClass().getResource("/images/rmlogo.png");
        Image logo = Image.getInstance(imageUrl);
        logo.setAbsolutePosition(328f, 782f);
        logo.scaleToFit(226, 120);
        canvas.addImage(logo, false);
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 14);
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("pdf.page.title"), 92, 818, 0);
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("pdf.page1.title2"), 92, 802, 0);
        canvas.endText();
        if (log.isDebugEnabled()) log.debug("loading image runes");
        Image raceRune = loadImage("/images/runes/race/" + sheet.getCulture().getRune());
        if (raceRune != null) {
            raceRune.setAbsolutePosition(495, 653);
            raceRune.scaleAbsolute(55f, 55f);
            canvas.addImage(raceRune, true);
        }
        Image profRune = loadImage("/images/runes/prof/" + sheet.getProfession().getRune());
        if (profRune != null) {
            profRune.setAbsolutePosition(495, 580);
            profRune.scaleAbsolute(55, 55);
            canvas.addImage(profRune, true);
        }
        if (log.isDebugEnabled()) log.debug("processing text page 1");
        float y = 0;
        drawNamesPage1(canvas);
        y = page1RaceProfArmorDB(canvas, 728f);
        y = page1Resistance(canvas, y, true);
        y = page1RaceAttributes(canvas, y);
        y = page1Characteristics(canvas, y);
        int lineNumb = (int) Math.floor((y - BOTTOM_Y) / PAGE1_LEFTBOX_LINE_HEIGHT);
        for (int i = 0; i < lineNumb; i++) {
            labeledUserText(canvas, "", "", LEFT_X + 4, y, PAGE1_LEFTBOX_RIGHTX - 4, fontRegular, 8);
            y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        }
        y = page1Stats(canvas);
        y = page1Favorites(canvas, y, 120, true);
        if (y > 131) {
            canvas.beginText();
            canvas.setFontAndSize(fontHeadline, 8);
            float centerX = PAGE1_RIGHTBOX_LEFTX + (RIGHT_X - PAGE1_RIGHTBOX_LEFTX) / 2;
            canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.header.notes"), centerX, y, 0);
            canvas.endText();
        }
        drawHitsPPExhaust(canvas, 111);
        footer(canvas);
    }
