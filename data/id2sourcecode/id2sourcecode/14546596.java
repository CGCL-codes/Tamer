        public HtmlCellBar makeCellList(List<? extends HtmlBarChartItem<? extends Number>> itemList) {
            HtmlCellBar htmlCellBar = new HtmlCellBar();
            if (itemList == null || itemList.size() == 0) {
                htmlCellBar.addHtmlCell(new DefaultHtmlCell(0, this.getLength(), null, null));
                return htmlCellBar;
            }
            HtmlBarChartItem<? extends Number> currentItem = null;
            HtmlBarChartItem<? extends Number> postItem = itemList.get(0);
            int currentPosition = Integer.MIN_VALUE;
            int postPosition = this.getPixelPosition(postItem.getValue().doubleValue());
            int preferredCellThickness = this.getPreferredCellThickness();
            int preferredCellLeftThickness = preferredCellThickness / 2;
            int preferredCellRightThickness = (preferredCellThickness + 1) / 2;
            int prevRightPosition = 0;
            for (int i = 0; i < itemList.size(); i++) {
                currentItem = postItem;
                if (i < itemList.size() - 1) {
                    postItem = itemList.get(i + 1);
                } else {
                    postItem = null;
                }
                currentPosition = postPosition;
                if (postItem != null) {
                    double postValue = postItem.getValue().doubleValue();
                    postPosition = this.getPixelPosition(postValue);
                } else {
                    postPosition = Integer.MAX_VALUE;
                }
                int currentLeftPosition = Math.max(prevRightPosition, currentPosition - preferredCellLeftThickness);
                int currentRightPosition;
                if (i == itemList.size() - 1) {
                    currentRightPosition = Math.min(currentPosition + preferredCellRightThickness, this.getLength());
                } else {
                    int postLeftPosition = postPosition - preferredCellLeftThickness;
                    int realCellRightThickness;
                    if (currentPosition + preferredCellRightThickness > postLeftPosition) {
                        realCellRightThickness = (postPosition - currentPosition + 1) / 2;
                    } else {
                        realCellRightThickness = preferredCellRightThickness;
                    }
                    currentRightPosition = currentPosition + realCellRightThickness;
                }
                if (currentLeftPosition - prevRightPosition > 0) {
                    DefaultHtmlCell emptyCell = new DefaultHtmlCell();
                    emptyCell.setStart(prevRightPosition);
                    emptyCell.setEnd(currentLeftPosition);
                    htmlCellBar.addHtmlCell(emptyCell);
                }
                DefaultHtmlCell realCell = new DefaultHtmlCell();
                realCell.setHtmlClass(currentItem.getHtmlClass());
                realCell.setHtmlId(currentItem.getHtmlId());
                realCell.setStart(currentLeftPosition);
                realCell.setEnd(currentRightPosition);
                htmlCellBar.addHtmlCell(realCell);
                prevRightPosition = currentRightPosition;
            }
            htmlCellBar.addHtmlCell(new DefaultHtmlCell(prevRightPosition, this.getLength(), null, null));
            return htmlCellBar;
        }
