    private float page1RaceAttributes(PdfContentByte canvas, float y) {
        float x = LEFT_X + 4;
        float x1 = PAGE1_LEFTBOX_RIGHTX - 4;
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 8);
        float center = LEFT_X + (PAGE1_LEFTBOX_RIGHTX - LEFT_X) / 2;
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.raceinfo.header"), center, y, 0);
        canvas.endText();
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("pdf.page1.raceinfo.souldeparture") + ":", "" + sheet.getRace().getSoulDeparture(), x, y, x1, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("pdf.page1.raceinfo.recoverymult") + ":", "" + sheet.getRace().getRecoveryMultiplier(), x, y, x1, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        IProgression progK = sheet.getProgressionBody();
        labeledUserText(canvas, RESOURCE.getString("ui.basic.progressionBody") + ":", progK.getFormattedString(), x, y, x1, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        IProgression progM = sheet.getProgressionPower();
        labeledUserText(canvas, RESOURCE.getString("ui.basic.progressionPower") + ":", progM.getFormattedString(), x, y, x1, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        hline(canvas, LEFT_X, y, PAGE1_LEFTBOX_RIGHTX);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        return y;
    }
