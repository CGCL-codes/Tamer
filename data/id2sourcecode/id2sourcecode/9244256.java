    private void page4Equipment(PdfContentByte canvas) {
        float lineHeight = 10.5f;
        float y = UPPER_Y - lineHeight;
        float centerX = LEFT_X + (RIGHT_X - LEFT_X) / 4;
        float[] x = new float[] { LEFT_X + 4, 238, 280 };
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 8);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.equipment"), centerX, y, 0);
        canvas.endText();
        y -= lineHeight;
        canvas.beginText();
        canvas.setFontAndSize(fontBold, 8);
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("common.equipment.itemdesc"), x[0], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.equipment.location"), x[1], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.equipment.weight"), x[2], y, 0);
        canvas.endText();
        y -= lineHeight;
        float yLine = y;
        canvas.beginText();
        canvas.setFontAndSize(fontRegular, 7);
        int maxEquipmentLines = 0;
        while (yLine > (BOTTOM_Y + 2 * lineHeight)) {
            maxEquipmentLines++;
            canvas.showTextAligned(Element.ALIGN_LEFT, "___________________________________________", x[0], yLine, 0);
            canvas.showTextAligned(Element.ALIGN_CENTER, "__________", x[1], yLine, 0);
            canvas.showTextAligned(Element.ALIGN_CENTER, "__________", x[2], yLine, 0);
            yLine -= lineHeight;
        }
        canvas.endText();
        List<Equipment> list = sheet.getEquipments();
        WeightUnit wu = sheet.getWeightUnit();
        int weightSumCustomUnit = 0;
        int weightSumCarriedCustUnit = 0;
        for (int i = 0; i < list.size() && i < maxEquipmentLines; i++) {
            showUserText(canvas, 7, x[0], y, list.get(i).getDescription());
            showUserText(canvas, 7, x[1], y, list.get(i).getPlace(), Element.ALIGN_CENTER);
            showUserText(canvas, 7, x[2], y, wu.getFormattedString(list.get(i).getWeight()) + (list.get(i).isCarried() ? "*" : ""), Element.ALIGN_CENTER);
            weightSumCustomUnit += list.get(i).getWeight();
            if (list.get(i).isCarried()) {
                weightSumCarriedCustUnit += list.get(i).getWeight();
            }
            y -= lineHeight;
        }
        labeledUserText(canvas, RESOURCE.getString("common.equipment.location.carried") + ":", wu.getFormattedString(weightSumCarriedCustUnit), x[0], yLine, centerX - 10, fontBold, 7);
        float pageCenterX = LEFT_X + (RIGHT_X - LEFT_X) / 2;
        labeledUserText(canvas, RESOURCE.getString("common.equipment.weight.total") + ":", wu.getFormattedString(weightSumCustomUnit), centerX + 10, yLine, pageCenterX - 4, fontBold, 7);
        yLine -= lineHeight;
        float encumbr = sheet.getEncumbranceBase();
        int factor = (int) Math.ceil(weightSumCarriedCustUnit / encumbr);
        int start = factor - 2;
        if (start < 1) {
            start = 1;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < (start + 4); i++) {
            if (sb.length() > 0) {
                sb.append(" | ");
            }
            if (factor == i) {
                sb.append("*");
            }
            float f1 = Math.round(10f * ((float) i - 1) * encumbr) / 10f;
            float f2 = Math.round(10f * i * encumbr) / 10f;
            sb.append(wu.getFormattedString(f1, false)).append(" - ").append(wu.getFormattedString(f2)).append(": ").append("" + ((i - 1) * -8));
        }
        canvas.beginText();
        canvas.setFontAndSize(fontRegular, 7);
        canvas.showTextAligned(Element.ALIGN_CENTER, sb.toString(), centerX, yLine, 0);
        canvas.endText();
    }
