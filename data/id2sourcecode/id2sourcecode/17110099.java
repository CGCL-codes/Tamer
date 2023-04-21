    private void calculateCursorPositions(MouseEvent e) {
        AClip c = Laoe.getInstance().getFocussedClipEditor().getClip();
        AClipPlotter clp = (AClipPlotter) c.getPlotter();
        ALayer l = c.getSelectedLayer();
        int i = l.getPlotter().getInsideChannelIndex(e.getPoint());
        if (i >= 0) {
            AChannel ch = l.getChannel(i);
            AChannelPlotter chp = ch.getPlotter();
            double x = chp.graphToSampleX(e.getPoint().x);
            float y = chp.graphToSampleY(e.getPoint().y);
            cursorPosition.setText("<html><b>" + GLanguage.translate("position") + "</b><br>" + ((float) clp.toPlotterXUnit(x)) + clp.getPlotterXUnitName() + " / " + ((float) clp.toPlotterYUnit(y)) + clp.getPlotterYUnitName() + "</html>");
        }
        GPlugin p = Laoe.getInstance().getPluginHandler().getFocussedPlugin();
        String s = GLanguage.translate("mouse");
        if (GToolkit.isShiftKey(e)) {
            s = s + "+" + GLanguage.translate("shift");
        }
        if (GToolkit.isCtrlKey(e)) {
            s = s + "+" + GLanguage.translate("ctrl");
        }
        keyMouseHelp.setText("<html><b>" + s + "</b><br>" + GLanguage.translate(p.getKeyMouseHelp(GToolkit.isShiftKey(e), GToolkit.isCtrlKey(e))) + "</html>");
    }
