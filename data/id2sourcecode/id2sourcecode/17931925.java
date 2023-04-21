    protected float page1Favorites(PdfContentByte canvas, float y, final float maxBottomY, boolean withEquipment) {
        List<List<Object>> favSkills = new ArrayList<List<Object>>();
        List<List<Object>> favSpelllists = new ArrayList<List<Object>>();
        List<List<Object>> favsWepaons = new ArrayList<List<Object>>();
        prepareFavorites(favSkills, favSpelllists, favsWepaons);
        float centerX = PAGE1_RIGHTBOX_LEFTX + (RIGHT_X - PAGE1_RIGHTBOX_LEFTX) / 2;
        float[] xVal = new float[] { PAGE1_RIGHTBOX_LEFTX + 4, centerX - 42, centerX - 17, centerX + 6, RIGHT_X - 42, RIGHT_X - 15 };
        float lineHeight = 10.5f;
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 8);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.skill.favs"), centerX, y, 0);
        y -= lineHeight;
        canvas.setFontAndSize(fontBold, 8);
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("common.skill"), xVal[0], y, 0);
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("common.skill"), xVal[3], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.ranks"), xVal[1], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.ranks"), xVal[4], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.bonus"), xVal[2], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.bonus"), xVal[5], y, 0);
        canvas.endText();
        y -= lineHeight;
        float yL = y;
        float yR = y;
        for (int k = 0; k < 2; k++) {
            List<List<Object>> favs = (k == 0) ? favSkills : favSpelllists;
            int firstRightIndex = getIndexOfHalfSpace(favs, lineHeight, centerX - PAGE1_RIGHTBOX_LEFTX, canvas);
            if (k == 1 && favs.size() > 0) {
                canvas.beginText();
                canvas.setFontAndSize(fontHeadline, 8);
                canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.spell.favs"), centerX, y, 0);
                canvas.endText();
                y -= lineHeight;
                yL = y;
                yR = y;
            }
            for (int idx = 0; y > maxBottomY && idx < favs.size(); idx++) {
                int c = 0;
                float y0 = yL;
                if (idx >= firstRightIndex) {
                    c = 3;
                    y0 = yR;
                }
                List<Object> line = favs.get(idx);
                for (int col = 0; col < 3; col++) {
                    String text = (col == 0) ? ((ISkill) line.get(col)).getName() : (String) line.get(col);
                    int align = (col == 0) ? Element.ALIGN_LEFT : Element.ALIGN_CENTER;
                    showUserText(canvas, 7, xVal[col + c], y0, text, align);
                    if (col == 0) {
                        labeledUserText(canvas, "", "", xVal[col + c] - 3, y0, xVal[col + c + 1] - 20, fontRegular, 7);
                    } else {
                        canvas.beginText();
                        canvas.setFontAndSize(fontRegular, 7);
                        canvas.showTextAligned(align, "_____", xVal[col + c], y0, 0);
                        canvas.endText();
                    }
                }
                @SuppressWarnings("unchecked") List<String> list = (List<String>) line.get(3);
                for (String itemLine : list) {
                    y0 = showMagicalItems(canvas, itemLine, y0, xVal[c], xVal[c + 2], false);
                }
                y0 -= lineHeight;
                if (idx >= firstRightIndex) {
                    yR = y0;
                } else {
                    yL = y0;
                }
                y = Math.min(yL, yR);
            }
        }
        if (favsWepaons.size() > 0 && y > maxBottomY) {
            hline(canvas, PAGE1_RIGHTBOX_LEFTX, y, RIGHT_X);
            y -= lineHeight;
            xVal = new float[] { 247, 360, 388, 414, 490 };
            canvas.beginText();
            canvas.setFontAndSize(fontHeadline, 8);
            canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.attack.favs"), centerX, y, 0);
            canvas.endText();
            y -= lineHeight;
            canvas.beginText();
            canvas.setFontAndSize(fontBold, 8);
            canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("pdf.page1.attack.descr"), xVal[0], y, 0);
            canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.ranks"), xVal[1], y, 0);
            canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.bonus"), xVal[2], y, 0);
            canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.fumble"), xVal[3], y, 0);
            canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.modifications"), xVal[4], y, 0);
            canvas.endText();
            y -= lineHeight;
            for (int idx = 0; idx < favsWepaons.size() && y > maxBottomY; idx++) {
                List<Object> weaponSkillArr = favsWepaons.get(idx);
                for (int col = 0; col < xVal.length; col++) {
                    String str = "";
                    if (col == 0) {
                        str = ((ISkill) weaponSkillArr.get(col)).getName();
                    } else if (col < weaponSkillArr.size()) {
                        str = (String) weaponSkillArr.get(col);
                    }
                    int align = (col == 0) ? Element.ALIGN_LEFT : Element.ALIGN_CENTER;
                    showUserText(canvas, (col < 4 ? 7 : 6), xVal[col], y, str, align);
                    if (col == 0) {
                        labeledUserText(canvas, "", "", xVal[col] - 3, y, xVal[col + 1] - 15, fontRegular, 7);
                    } else {
                        canvas.beginText();
                        canvas.setFontAndSize(fontRegular, 7);
                        if (col + 1 == xVal.length) {
                            canvas.showTextAligned(Element.ALIGN_RIGHT, "__________________________________", RIGHT_X - 4, y, 0);
                        } else {
                            canvas.showTextAligned(align, "______", xVal[col], y, 0);
                        }
                        canvas.endText();
                    }
                }
                @SuppressWarnings("unchecked") List<String> items = (List<String>) weaponSkillArr.get(5);
                for (String item : items) {
                    y = showMagicalItems(canvas, item, y, xVal[0], RIGHT_X - 4, false);
                }
                y -= lineHeight;
            }
        }
        if (withEquipment) {
            List<Equipment> equipFavs = new ArrayList<Equipment>();
            for (Equipment eq : sheet.getEquipments()) {
                if (eq.isFavorite()) {
                    equipFavs.add(eq);
                }
            }
            if (equipFavs.size() > 0 && y > maxBottomY) {
                hline(canvas, PAGE1_RIGHTBOX_LEFTX, y, RIGHT_X);
                y -= lineHeight;
                canvas.beginText();
                canvas.setFontAndSize(fontHeadline, 8);
                canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.equipment.favs"), centerX, y, 0);
                canvas.endText();
                y -= lineHeight;
                canvas.beginText();
                canvas.setFontAndSize(fontBold, 8);
                canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("common.equipment.itemdesc"), 247, y, 0);
                canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.equipment.location"), 485, y, 0);
                canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.equipment.weight"), 530, y, 0);
                canvas.endText();
                y -= lineHeight;
                for (int i = 0; i < equipFavs.size() && y > maxBottomY; i++) {
                    Equipment eq = equipFavs.get(i);
                    labeledUserText(canvas, "", eq.getDescription(), 244, y, 460, fontRegular, 7);
                    showUserText(canvas, 7, 485, y, eq.getPlace(), Element.ALIGN_CENTER);
                    showUserText(canvas, 7, 530, y, sheet.getWeightUnit().getFormattedString(eq.getWeight()), Element.ALIGN_CENTER);
                    canvas.beginText();
                    canvas.setFontAndSize(fontRegular, 7);
                    canvas.showTextAligned(Element.ALIGN_CENTER, "___________", 485, y, 0);
                    canvas.showTextAligned(Element.ALIGN_CENTER, "__________", 530, y, 0);
                    canvas.endText();
                    y -= lineHeight;
                }
            }
            if (y > (maxBottomY + lineHeight)) {
                hline(canvas, PAGE1_RIGHTBOX_LEFTX, y, RIGHT_X);
                y -= lineHeight;
            }
        }
        return y;
    }
