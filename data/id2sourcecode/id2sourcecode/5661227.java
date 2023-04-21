    private float page1Stats(PdfContentByte canvas, final float initialY) {
        float LINE_HEIGHT = 14.5f;
        float y = initialY;
        float centerX = PAGE1_RIGHTBOX_LEFTX + (RIGHT_X - PAGE1_RIGHTBOX_LEFTX) / 2;
        float[] xVal = new float[] { PAGE1_RIGHTBOX_LEFTX + 10, 337, 365 };
        canvas.beginText();
        canvas.setFontAndSize(fontBold, 8);
        String[] totalBonus = StringUtils.split(RESOURCE.getString("pdf.page1.stat.bonus.total"));
        int totalBonusIdx = 0;
        if (totalBonus.length > 1) {
            canvas.showTextAligned(Element.ALIGN_CENTER, totalBonus[totalBonusIdx++], xVal[2], y, 0);
        }
        y -= 10;
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("pdf.page1.stat.header"), xVal[0], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.stat.temp"), xVal[1], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, totalBonus[totalBonusIdx], xVal[2], y, 0);
        canvas.endText();
        y -= LINE_HEIGHT;
        int idx = 0;
        for (StatEnum stat : StatEnum.values()) {
            float xModi = idx < 5f ? 0 : (centerX - PAGE1_RIGHTBOX_LEFTX + 10);
            for (int col = 0; col < xVal.length; col++) {
                String str = null;
                switch(col) {
                    case 0:
                        canvas.beginText();
                        String statStr = stat.getFullI18N();
                        canvas.setFontAndSize(fontRegular, 8);
                        canvas.showTextAligned(Element.ALIGN_LEFT, statStr, xVal[col] + xModi, y, 0);
                        canvas.endText();
                        break;
                    case 1:
                        str = "" + sheet.getStatTemp(stat);
                        break;
                    case 2:
                        str = format(sheet.getStatBonusTotal(stat), false);
                        box(canvas, xVal[col] - 9 + xModi, y + 8, xVal[col] + 10 + xModi, y - 3);
                        break;
                    default:
                        str = "";
                }
                if (col > 0 && col < (xVal.length - 1)) {
                    showUserText(canvas, 8, xVal[col] + xModi, y, "___", Element.ALIGN_CENTER);
                }
                showUserText(canvas, 8, xVal[col] + xModi, y, str, Element.ALIGN_CENTER);
            }
            y -= LINE_HEIGHT;
            idx++;
            if (idx == 5) {
                y = y + 5 * LINE_HEIGHT;
            }
        }
        hline(canvas, PAGE1_RIGHTBOX_LEFTX, y, RIGHT_X);
        y -= LINE_HEIGHT;
        return y;
    }
