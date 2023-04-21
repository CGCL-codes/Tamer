    @Override
    public void modelChanged(Model model, int channel, boolean moreToCome) {
        switch(channel) {
            case PlotPanel3D.X_CHANNEL:
                if (model.hasData(channel)) {
                    String text = "<html><b><u>" + I18n.get("PlotView.Mappings.XAxis") + "</u></b><br>" + model.getChannelTitle(channel) + "<br>" + I18n.get("PlotView.Legend.Domain") + ": " + model.getDataMin(channel) + " - " + model.getDataMax(channel) + "</html>";
                    xLabel.setText(text);
                    xLabel.setVisible(true);
                } else {
                    xLabel.setText(" ");
                    xLabel.setVisible(false);
                }
                break;
            case PlotPanel3D.Y_CHANNEL:
                if (model.hasData(channel)) {
                    String text = "<html><b><u>" + I18n.get("PlotView.Mappings.YAxis") + "</u></b><br>" + model.getChannelTitle(channel) + "<br>" + I18n.get("PlotView.Legend.Domain") + ": " + model.getDataMin(channel) + " - " + model.getDataMax(channel) + "</html>";
                    yLabel.setText(text);
                    yLabel.setVisible(true);
                } else {
                    yLabel.setText(" ");
                    yLabel.setVisible(false);
                }
                break;
            case PlotPanel3D.Z_CHANNEL:
                if (model.hasData(channel)) {
                    String text = "<html><b><u>" + I18n.get("PlotView.Mappings.ZAxis") + "</u></b><br>" + model.getChannelTitle(channel) + "<br>" + I18n.get("PlotView.Legend.Domain") + ": " + model.getDataMin(channel) + " - " + model.getDataMax(channel) + "</html>";
                    zLabel.setText(text);
                    zLabel.setVisible(true);
                } else {
                    zLabel.setText(" ");
                    zLabel.setVisible(false);
                }
                break;
            case PlotPanel3D.COLOR_CHANNEL:
                if (model.hasData(channel)) {
                    String text = "<html><b><u>" + I18n.get("PlotView.DotColor") + "</u></b><br>" + model.getChannelTitle(channel) + "<br>" + I18n.get("PlotView.Legend.Domain") + ": " + model.getDataMin(channel) + " - " + model.getDataMax(channel) + "</html>";
                    colorLabel.setText(text);
                    colorDistribution.setStartValue(model.getDataMin(channel));
                    colorDistribution.setEndValue(model.getDataMax(channel));
                    colorPanel.setVisible(true);
                } else {
                    colorLabel.setText(" ");
                    colorDistribution.setStartValue(0.0);
                    colorDistribution.setEndValue(1.0);
                    colorPanel.setVisible(false);
                }
                colorPanel.repaint();
                break;
            case PlotPanel3D.SIZE_CHANNEL:
                if (model.hasData(channel)) {
                    String text = "<html><b><u>" + I18n.get("PlotView.DotSize") + "</u></b><br>" + model.getChannelTitle(channel) + "<br>" + I18n.get("PlotView.Legend.Domain") + ": " + model.getDataMin(channel) + " - " + model.getDataMax(channel) + "</html>";
                    sizeLabel.setText(text);
                    sizeDistribution.setStartValue(model.getDataMin(channel));
                    sizeDistribution.setEndValue(model.getDataMax(channel));
                    sizePanel.setVisible(true);
                } else {
                    sizeLabel.setText(" ");
                    sizeDistribution.setStartValue(0.0);
                    sizeDistribution.setEndValue(1.0);
                    sizePanel.setVisible(false);
                }
                sizePanel.repaint();
                break;
        }
    }
