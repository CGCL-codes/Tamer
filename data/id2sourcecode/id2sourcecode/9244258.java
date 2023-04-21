    private float page1Characteristics(PdfContentByte canvas, float y) {
        if (sheet.getCharacteristics() == null) return y;
        float x1 = LEFT_X + 4;
        float x2 = PAGE1_LEFTBOX_RIGHTX - 3;
        float centerX = x1 + (x2 - x1) / 2;
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 8);
        float center = LEFT_X + (PAGE1_LEFTBOX_RIGHTX - LEFT_X) / 2;
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.characteristics.header"), center, y, 0);
        canvas.endText();
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        Characteristics ch = sheet.getCharacteristics();
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.demeanor") + ":", ch.getDemeanor(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.appearance") + ":", "" + ch.getAppearance(), x1, y, centerX - 15, fontRegular, 8);
        String ageLabel = "";
        String ageText = "";
        if (StringUtils.trimToNull(ch.getApparentlyAge()) != null) {
            ageText = "(" + ch.getApparentlyAge() + ") ";
            ageLabel = "(" + RESOURCE.getString("rolemaster.characteristics.appage") + ") ";
        }
        ageLabel += RESOURCE.getString("rolemaster.characteristics.age") + ":";
        ageText += ch.getAge();
        labeledUserText(canvas, ageLabel, ageText, centerX - 13, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.gender") + ":", ch.isFemale() ? RESOURCE.getString("gender.female") : RESOURCE.getString("gender.male"), x1, y, centerX - 1, fontRegular, 8);
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.skin") + ":", ch.getSkin(), centerX + 1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.height") + ":", sheet.getLengthUnit().getFormattedString(ch.getHeight()), x1, y, centerX - 1, fontRegular, 8);
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.weight") + ":", sheet.getWeightUnit().getFormattedString(ch.getWeight()), centerX + 1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.hairColor") + ":", ch.getHairColor(), x1, y, centerX - 1, fontRegular, 8);
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.eyeColor") + ":", ch.getEyeColor(), centerX + 1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        if (!StringUtils.isEmpty(sheet.getCharacteristics().getClothSize())) {
            labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.clothSize") + ":", sheet.getCharacteristics().getClothSize(), x1, y, x2, fontRegular, 8);
            y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        }
        if (!StringUtils.isEmpty(sheet.getCharacteristics().getHatSize()) || !StringUtils.isEmpty(sheet.getCharacteristics().getShoeSize())) {
            labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.hatSize") + ":", sheet.getCharacteristics().getHatSize(), x1, y, centerX - 1, fontRegular, 8);
            labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.shoeSize") + ":", sheet.getCharacteristics().getShoeSize(), centerX + 1, y, x2, fontRegular, 8);
            y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        }
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.personality") + ":", ch.getPersonality(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.motivation") + ":", ch.getMotivation(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.alignment") + ":", ch.getAlignment(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        hline(canvas, LEFT_X, y, PAGE1_LEFTBOX_RIGHTX);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 8);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.background.header"), center, y, 0);
        canvas.endText();
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.nationality") + ":", ch.getNationality(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.homeTown") + ":", ch.getHomeTown(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.deity") + ":", ch.getDeity(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.lord") + ":", ch.getLord(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.parent") + ":", ch.getParent(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.spouse") + ":", ch.getSpouse(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.siblings") + ":", ch.getSiblings(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.children") + ":", ch.getChildren(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.misc") + ":", ch.getMisc1(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, "", ch.getMisc2(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, "", ch.getMisc3(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, "", ch.getMisc4(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        return y;
    }
