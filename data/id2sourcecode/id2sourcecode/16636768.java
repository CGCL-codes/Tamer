    boolean moveDelta(int index, int dx, int dy) {
        if (dx == 0 && dy == 0) return false;
        boolean needResize = false;
        boolean needLayout = false;
        if (dy == 0) {
            int[] ws = new int[items.length];
            for (int i = 0; i < ws.length; i++) {
                ws[i] = items[i].idealWidth;
            }
            boolean needCalculate = false;
            CoolItem item = items[index];
            if (item.wrap && (dx < 0 || isLastItemOfRow(index))) {
                return false;
            }
            if ((index == 0 && items.length > 1) || (item.wrap && !isLastItemOfRow(index))) {
                if (dx >= item.lastCachedWidth) {
                    CoolItem next = items[index + 1];
                    items[index] = next;
                    items[index + 1] = item;
                    if (item.wrap) {
                        next.wrap = true;
                        item.wrap = false;
                    }
                    int width = next.idealWidth;
                    next.idealWidth = item.idealWidth;
                    item.idealWidth = width;
                    dx = dx - item.lastCachedWidth;
                    index++;
                    needLayout = true;
                }
            }
            if (dx != 0 && index > 0 && !(item.wrap && !isLastItemOfRow(index))) {
                CoolItem cur = item;
                CoolItem prev = items[index - 1];
                int idx = index - 1;
                while (dx < 0) {
                    if (prev.lastCachedWidth + dx < minWidth(prev)) {
                        int ddx = prev.lastCachedWidth - minWidth(prev);
                        prev.idealWidth -= ddx;
                        item.idealWidth += ddx;
                        needCalculate = true;
                        dx += ddx;
                        if (dx < 0) {
                            if (idx - 1 >= 0 && !items[idx].wrap) {
                                idx--;
                                prev = items[idx];
                            } else {
                                if (dx + 11 <= 0) {
                                    CoolItem swpItem = prev;
                                    int swpIndex = index;
                                    while (dx + minWidth(swpItem) <= 0) {
                                        dx += minWidth(swpItem);
                                        swpItem = items[swpIndex - 1];
                                        items[swpIndex - 1] = items[swpIndex];
                                        items[swpIndex] = swpItem;
                                        if (swpItem.wrap) {
                                            items[swpIndex - 1].wrap = true;
                                            swpItem.wrap = false;
                                        }
                                        needLayout = true;
                                        swpIndex--;
                                        if (swpIndex == 0 || swpItem.wrap) {
                                            break;
                                        }
                                    }
                                }
                                dx = 0;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
                CoolItem next = null;
                idx = index;
                while (dx > 0 && cur.lastCachedWidth - dx < minWidth(cur)) {
                    int dxx = cur.lastCachedWidth - minWidth(cur);
                    prev.idealWidth += dxx;
                    cur.idealWidth -= dxx;
                    needCalculate = true;
                    dx -= dxx;
                    if (dx > 0) {
                        if (idx + 1 < items.length && !isLastItemOfRow(idx)) {
                            idx++;
                            cur = items[idx];
                            if (next == null) {
                                next = cur;
                            }
                        } else {
                            if (dx >= 11 && next != null) {
                                CoolItem swpItem = next;
                                int swpIndex = index;
                                while (dx >= minWidth(swpItem)) {
                                    items[swpIndex + 1] = items[swpIndex];
                                    items[swpIndex] = swpItem;
                                    if (swpItem.wrap) {
                                        items[swpIndex].wrap = true;
                                        swpItem.wrap = false;
                                    }
                                    swpItem = items[swpIndex + 1];
                                    needLayout = true;
                                    dx -= minWidth(swpItem);
                                    swpIndex++;
                                    if (swpIndex >= items.length || isLastItemOfRow(swpIndex)) {
                                        break;
                                    }
                                }
                            }
                            dx = 0;
                            break;
                        }
                    }
                }
                prev.idealWidth += dx;
                if (dx != 0) {
                    needCalculate = true;
                }
                if (item != cur) {
                    if (cur.idealWidth - dx < 0) {
                        if (cur.idealWidth != 0) {
                            needCalculate = true;
                        }
                        cur.idealWidth = 0;
                    } else {
                        cur.idealWidth -= dx;
                    }
                } else {
                    item.idealWidth -= dx;
                }
            }
            if (needCalculate && !needLayout) {
                for (int i = 0; i < ws.length; i++) {
                    if (ws[i] != items[i].idealWidth) {
                        needLayout = true;
                        break;
                    }
                }
            }
        } else {
            int line = verticalLine(index);
            if (line + dy < 0) {
                if (index == 0 && isLastItemOfRow(index)) {
                } else {
                    CoolItem ci = items[index];
                    if ((index == 0 && items.length > 1) || (ci.wrap && index < items.length - 1)) {
                        items[index + 1].wrap = true;
                    }
                    for (int i = index; i > 0; i--) {
                        items[i] = items[i - 1];
                    }
                    items[0] = ci;
                    items[1].wrap = true;
                    ci.wrap = false;
                    needLayout = true;
                    needResize = true;
                }
            } else if (line + dy < getVerticalLines()) {
                int lineNumber = line + dy;
                int i = 0;
                for (i = 0; i < items.length; i++) {
                    if (lineNumber == 0) {
                        break;
                    }
                    if (items[i].wrap) {
                        lineNumber--;
                    }
                }
                if (i > 0) i--;
                CoolItem ci = items[index];
                if (index == 0 && isLastItemOfRow(index)) {
                    needResize = true;
                }
                if (ci.wrap) {
                    if (isLastItemOfRow(index)) {
                        needResize = true;
                    }
                    if (index < items.length - 1) {
                        items[index + 1].wrap = true;
                    }
                }
                int x = ci.getPosition().x + dx;
                if (x <= 0) {
                    if (i == 0) {
                        ci.wrap = false;
                    } else {
                        if (index == 0 && i == 1) {
                        } else {
                            ci.wrap = true;
                        }
                        if (i < items.length - 1) {
                            items[i + 1].wrap = false;
                        }
                    }
                } else {
                    int rowWidth = 0;
                    int separator = 2;
                    for (; i < items.length; i++) {
                        CoolItem item = items[i];
                        int minimum = item.minimumWidth + (item.minimumWidth != 0 ? 2 : 0);
                        rowWidth += 7 + 2 + Math.max(item.idealWidth, minimum) + separator;
                        int xx = item.getPosition().x;
                        if (xx < x && (x <= rowWidth || isLastItemOfRow(i))) {
                            item.idealWidth = Math.max(0, x - xx - (7 + 2 + minimum + separator));
                            minimum = ci.minimumWidth + (ci.minimumWidth != 0 ? 2 : 0);
                            int mw = 7 + 2 + minimum + separator;
                            ci.idealWidth = Math.max(item.minimumWidth, Math.max(ci.idealWidth, rowWidth - x - mw));
                            if (rowWidth - x - mw < ci.idealWidth) {
                                needResize = true;
                            }
                            break;
                        }
                    }
                    ci.wrap = false;
                }
                if (dy < 0 && x > 0 && i < items.length - 1) {
                    i++;
                }
                if (dy > 0) {
                    for (int j = index; j < i; j++) {
                        items[j] = items[j + 1];
                    }
                } else {
                    for (int j = index; j > i; j--) {
                        items[j] = items[j - 1];
                    }
                }
                items[i] = ci;
                items[0].wrap = false;
                needLayout = true;
            } else {
                if ((items[index].wrap || index == 0) && isLastItemOfRow(index)) {
                } else {
                    CoolItem ci = items[index];
                    if (index > 0 && ci.wrap) {
                        items[index + 1].wrap = true;
                    }
                    for (int i = index; i < items.length - 1; i++) {
                        items[i] = items[i + 1];
                    }
                    items[items.length - 1] = ci;
                    ci.wrap = true;
                    needLayout = true;
                    needResize = true;
                }
            }
        }
        int w = width;
        int h = height;
        if (needResize) {
            Point computeSize = computeSize(-1, -1, false);
            w = computeSize.x;
            h = computeSize.y;
        }
        if (needLayout) {
            SetWindowPos(handle, null, left, top, width, h, -1);
        }
        if (w > width) {
            for (int i = index; i < items.length; i++) {
                if (isLastItemOfRow(i)) {
                    moveDelta(i, width - height, 0);
                    break;
                }
            }
        }
        if (h != height && !ignoreResize) {
            setBounds(left, top, Math.max(0, width), Math.max(0, h), SWT.NONE);
            sendEvent(SWT.Resize);
        }
        return needLayout;
    }
