        public synchronized void paintComponent(Graphics g) {
            big = (Graphics2D) g;
            FontMetrics fm = big.getFontMetrics(font);
            ascent = (int) fm.getAscent();
            descent = (int) fm.getDescent();
            big.setBackground(getBackground());
            big.clearRect(0, 0, w, h);
            float totalMemory = (float) r.totalMemory();
            float freeMemory = (float) r.freeMemory();
            float usedMemory = (float) totalMemory - freeMemory;
            if (totalMemoryPrev != totalMemory) updatePts = true;
            big.setColor(Color.green);
            big.drawString(String.valueOf((int) totalMemory / 1024) + "K allocated", 4.0f, (float) ascent + 0.5f);
            usedStr = String.valueOf(((int) (usedMemory)) / 1024) + "K used";
            big.drawString(usedStr, 4, h - descent);
            float ssH = ascent + descent;
            float remainingHeight = (float) (h - (ssH * 2) - 0.5f);
            float blockHeight = remainingHeight / 10;
            float blockWidth = 20.0f;
            float remainingWidth = (float) (w - blockWidth - 10);
            big.setColor(mfColor);
            int MemUsage = (int) ((freeMemory / totalMemory) * 10);
            int i = 0;
            for (; i < MemUsage; i++) {
                mfRect.setRect(5, (float) ssH + i * blockHeight, blockWidth, (float) blockHeight - 1);
                big.fill(mfRect);
            }
            big.setColor(Color.green);
            for (; i < 10; i++) {
                muRect.setRect(5, (float) ssH + i * blockHeight, blockWidth, (float) blockHeight - 1);
                big.fill(muRect);
            }
            big.setColor(graphColor);
            int graphX = 30;
            int graphY = (int) ssH;
            int graphW = w - graphX - 5;
            int graphH = (int) remainingHeight;
            if (graphW <= 0 || graphH <= 0) {
                System.out.println("size = " + getSize());
                System.out.println("w = " + w + ", h = " + h);
                System.out.println("graphW = " + graphW + ", graphH = " + graphH);
                return;
            }
            graphOutlineRect.setRect(graphX, graphY, graphW, graphH);
            big.draw(graphOutlineRect);
            int graphRow = graphH / 10;
            for (int j = graphY; j <= graphH + graphY; j += graphRow) {
                graphLine.setLine(graphX, j, graphX + graphW, j);
                big.draw(graphLine);
            }
            int graphColumn = graphW / 15;
            if (animating()) {
                if (columnInc == 0) {
                    columnInc = graphColumn;
                }
                --columnInc;
            }
            for (int j = graphX + columnInc; j < graphW + graphX; j += graphColumn) {
                graphLine.setLine(j, graphY, j, graphY + graphH);
                big.draw(graphLine);
            }
            if (pts == null) {
                pts = new int[graphW];
                values = new float[graphW];
                ptNum = 0;
            } else if (pts.length != graphW) {
                int tmp[] = null;
                float tmpf[] = null;
                if (ptNum < graphW) {
                    tmp = new int[ptNum];
                    System.arraycopy(pts, 0, tmp, 0, ptNum);
                    tmpf = new float[ptNum];
                    System.arraycopy(values, 0, tmpf, 0, ptNum);
                } else {
                    tmp = new int[graphW];
                    System.arraycopy(pts, ptNum - graphW, tmp, 0, graphW);
                    tmpf = new float[graphW];
                    System.arraycopy(values, ptNum - graphW, tmpf, 0, graphW);
                    ptNum = graphW;
                }
                pts = new int[graphW];
                System.arraycopy(tmp, 0, pts, 0, tmp.length);
                values = new float[graphW];
                System.arraycopy(tmpf, 0, values, 0, tmpf.length);
            } else {
                if (ptNum > values.length) {
                    System.out.println("size = " + getSize());
                    System.out.println("w = " + w + ", h = " + h);
                    System.out.println("graphW = " + graphW + ", graphH = " + graphH);
                    System.out.println("ptNum = " + ptNum);
                    return;
                }
                big.setColor(Color.yellow);
                if (animating()) {
                    if (ptNum == graphW) {
                        ptNum--;
                        for (int j = 0; j < ptNum; j++) {
                            pts[j] = pts[j + 1];
                            values[j] = values[j + 1];
                        }
                    }
                    values[ptNum] = usedMemory;
                    pts[ptNum] = (int) (graphY + graphH * (totalMemory - values[ptNum]) / totalMemory);
                    ptNum++;
                }
            }
            if (updatePts) {
                for (int k = 0; k < ptNum; k++) pts[k] = (int) (graphY + graphH * (totalMemory - values[k]) / totalMemory);
                updatePts = false;
            }
            for (int j = graphX + graphW - ptNum + 1, k = 1; k < ptNum; k++, j++) {
                big.drawLine(j - 1, pts[k - 1], j, pts[k]);
            }
        }
