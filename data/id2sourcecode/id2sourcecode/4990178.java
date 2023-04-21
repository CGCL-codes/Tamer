    private void printPageFooter(Graphics g, PageFormat pf, int pi) {
        Font f = new Font("Default", Font.BOLD, fontSize);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        String strName = "";
        try {
            strName = tournament.getTournamentParameterSet().getGeneralParameterSet().getName();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        String strLeft = Gotha.getGothaVersionnedName() + " : " + strName + "    ";
        String strCenter = "Page" + " " + (pi + 1) + "/" + numberOfPages;
        char[] tcLeft = strLeft.toCharArray();
        char[] tcCenter = strCenter.toCharArray();
        int wLeft = fm.charsWidth(tcLeft, 0, tcLeft.length);
        int wCenter = fm.charsWidth(tcCenter, 0, tcCenter.length);
        while (wLeft + wCenter / 2 > usableWidth / 2) {
            if (strLeft.length() <= 2) break;
            strLeft = strLeft.substring(0, strLeft.length() - 2);
            tcLeft = strLeft.toCharArray();
            wLeft = fm.charsWidth(tcLeft, 0, tcLeft.length);
        }
        strLeft = strLeft.substring(0, strLeft.length() - 2);
        g.drawString(strLeft, usableX, usableY + usableHeight - fm.getDescent());
        int x = usableX + (usableWidth - wCenter) / 2;
        g.drawString(strCenter, x, usableY + usableHeight - fm.getDescent());
        java.util.Date dh = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm  ");
        String strDH = sdf.format(dh);
        String strRight = strDH;
        x = usableX + usableWidth;
        drawRightAlignedString(g, strRight, x, usableY + usableHeight - fm.getDescent());
    }
