        private void drawItem(Graphics2D g2D, ScheduleItem item, Color boxColor, Color fontColor) {
            int y;
            int h = 0;
            PERangeList gridletPERanges = item.getPERangeList();
            gridletPERanges.sortRanges();
            int firstX, firstY;
            int width, height;
            int textX, textY;
            int textHeight, textWidth;
            double duration = item.getActualFinishTime() - item.getStartTime();
            width = (int) (duration * scaleX_);
            firstX = SHIFT_X + (int) (item.getStartTime() * scaleX_);
            String boxText;
            LineMetrics lineMetrics;
            Font font = g2D.getFont().deriveFont(10f);
            g2D.setFont(font);
            FontRenderContext frc = g2D.getFontRenderContext();
            for (PERange range : gridletPERanges) {
                y = range.getEnd();
                h = range.getNumPE();
                firstY = SHIFT_Y + (int) ((numPE - (y + 1)) * scaleY_);
                height = (int) ((h) * scaleY_);
                boolean reservedGridlet = !item.isAdvanceReservation() && item.hasReserved();
                Composite previousComposite = null;
                if (reservedGridlet) {
                    previousComposite = g2D.getComposite();
                    g2D.setComposite(transpComp);
                }
                g2D.setColor(boxColor);
                if (!reservedGridlet) g2D.fillRect(firstX, firstY, width, height);
                g2D.setColor(Color.black);
                g2D.drawRect(firstX, firstY, width, height);
                boxText = new Integer(item.getID()).toString();
                textWidth = (int) font.getStringBounds(boxText, frc).getWidth();
                lineMetrics = font.getLineMetrics(boxText, frc);
                textHeight = (int) (lineMetrics.getAscent() + lineMetrics.getDescent());
                textX = firstX + (width - textWidth) / 2;
                textY = (int) (firstY + (height + textHeight) / 2 - lineMetrics.getDescent());
                g2D.setColor(fontColor);
                if (drawID_) {
                    g2D.drawString(boxText, textX, textY);
                }
                if (reservedGridlet) {
                    g2D.setComposite(previousComposite);
                }
            }
        }
