    private boolean isSameLooseGroup(ClosestPoints cpts, ClosestPointGroup cpg1, ClosestPointGroup cpg2, Board board) {
        Stone[] stones1 = cpg1.getStones();
        Stone[] stones2 = cpg2.getStones();
        Color col = stones1[0].getColor();
        Color oppcolor = col.flip();
        if (!stones1[0].getColor().equals(stones2[0].getColor())) return false;
        StoneDistance[] sdarr = getStoneDistances(stones1, stones2);
        ArrayList lgstones = new ArrayList();
        boolean flag = false;
        for (int i = 0; i < sdarr.length; i++) {
            Stone st1 = sdarr[i].getStone1();
            Stone st2 = sdarr[i].getStone2();
            int x1 = st1.getX();
            int y1 = st1.getY();
            int y2 = st2.getY();
            int x2 = st2.getX();
            int hor = sdarr[i].getHorizontal();
            int vert = sdarr[i].getVertical();
            if (sdarr[i].compare(0, 0) == 0) {
                SystemLog.warning("In LGImpl.isSameLooseGroup(), distance == 0");
                continue;
            } else if (sdarr[i].compare(1, 1) == 0) {
                Point pt1 = Point.get(x1, y2);
                Point pt2 = Point.get(x2, y1);
                Color c1 = board.getColor(pt1);
                Color c2 = board.getColor(pt2);
                int oppcount = 0;
                Point centerPt = null;
                if (col.flip().equals(c1)) {
                    oppcount++;
                    centerPt = pt2;
                }
                if (col.flip().equals(c2)) {
                    oppcount++;
                    centerPt = pt1;
                }
                if (oppcount == 0) return true;
                if (oppcount == 2) continue;
                if (checkPonnuki(centerPt, col, board)) return true;
            } else if (sdarr[i].compare(2, 0) == 0) {
                flag = true;
                int cx = (x1 + x2) / 2;
                int cy = (y1 + y2) / 2;
                Stone center = Stone.get(col, cx, cy);
                if (board.getColor(center).equals(oppcolor)) flag = false;
                if (x1 == cx) {
                    if (x1 > 0) {
                        Point pt = Point.get(x1, cy);
                        if (board.getColor(pt).equals(oppcolor)) flag = false;
                    }
                    if (x1 < 18) {
                        Point pt = Point.get(x1 + 1, cy);
                        if (board.getColor(pt).equals(oppcolor)) flag = false;
                    }
                } else {
                    if (y1 > 0) {
                        Point pt = Point.get(cx, y1 - 1);
                        if (board.getColor(pt).equals(oppcolor)) flag = false;
                    }
                    if (y1 < 18) {
                        Point pt = Point.get(cx, y1 + 1);
                        if (board.getColor(pt).equals(oppcolor)) flag = false;
                    }
                }
            } else if ((sdarr[i].compare(2, 1) == 0) || (sdarr[i].compare(2, 2) == 0)) {
                flag = true;
                int minx = Math.min(x1, x2);
                int maxx = Math.max(x1, x2);
                int miny = Math.min(y1, y2);
                int maxy = Math.max(y1, y2);
                for (int j = minx; j <= maxx; j++) {
                    for (int k = miny; k <= maxy; k++) {
                        Point pt = Point.get(j, k);
                        if (board.getColor(pt).equals(oppcolor)) {
                            flag = false;
                            break;
                        }
                    }
                }
            } else if ((sdarr[i].compare(3, 0) == 0) || (sdarr[i].compare(3, 1) == 0)) {
                lgstones.add(sdarr[i].getStone1());
                lgstones.add(sdarr[i].getStone2());
                if (checkEmptyPoints(cpts, lgstones, col, cpg1, cpg2)) {
                    flag = true;
                    break;
                }
            } else if (sdarr[i].compare(4, 0) == 0) {
                if ((hor == 4) || (hor == -4)) {
                    if (y1 < 18) {
                        Stone tent1 = Stone.get(col, x1, y1 + 1);
                        Stone tent2 = Stone.get(col, x2, y1 + 1);
                        if (process3JumpCase(tent1, tent2, i, sdarr, lgstones)) {
                            if (checkEmptyPoints(cpts, lgstones, col, cpg1, cpg2)) {
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (y1 > 0) {
                        Stone tent1 = Stone.get(col, x1, y1 - 1);
                        Stone tent2 = Stone.get(col, x2, y1 - 1);
                        if (process3JumpCase(tent1, tent2, i, sdarr, lgstones)) {
                            if (checkEmptyPoints(cpts, lgstones, col, cpg1, cpg2)) {
                                flag = true;
                                break;
                            }
                        }
                    }
                } else {
                    if (x1 < 18) {
                        Stone tent1 = Stone.get(col, x1 + 1, y1);
                        Stone tent2 = Stone.get(col, x2 + 1, y1);
                        if (process3JumpCase(tent1, tent2, i, sdarr, lgstones)) {
                            if (checkEmptyPoints(cpts, lgstones, col, cpg1, cpg2)) {
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (x1 > 0) {
                        Stone tent1 = Stone.get(col, x1 - 1, y1);
                        Stone tent2 = Stone.get(col, x2 - 1, y1);
                        if (process3JumpCase(tent1, tent2, i, sdarr, lgstones)) {
                            if (checkEmptyPoints(cpts, lgstones, col, cpg1, cpg2)) {
                                flag = true;
                                break;
                            }
                        }
                    }
                }
            } else if (sdarr[i].compare(4, 1) == 0) {
                ArrayList tent = new ArrayList();
                if (hor == 4) {
                    if (x1 > 0) tent.add(Stone.get(col, x1 - 1, y2));
                    if (x2 < 18) tent.add(Stone.get(col, x2 + 1, y1));
                } else if (hor == -4) {
                    if (x1 < 18) tent.add(Stone.get(col, x1 + 1, y2));
                    if (x2 > 0) tent.add(Stone.get(col, x2 - 1, y1));
                } else if (vert == 4) {
                    if (y1 > 0) tent.add(Stone.get(col, x2, y1 - 1));
                    if (y2 < 18) tent.add(Stone.get(col, x1, y2 + 1));
                } else if (vert == -4) {
                    if (y1 < 18) tent.add(Stone.get(col, x2, y1 + 1));
                    if (y2 > 0) tent.add(Stone.get(col, x1, y2 - 1));
                }
                Stone origst1 = sdarr[i].getStone1();
                Stone origst2 = sdarr[i].getStone2();
                int index = i + 1;
                while (index < sdarr.length) {
                    int cval = sdarr[index].compare(5, 0);
                    if (cval > 0) break; else if (cval == 0) {
                        for (int j = 0; j < tent.size(); j++) {
                            Stone st = (Stone) tent.get(j);
                            Stone testst1 = sdarr[index].getStone1();
                            Stone testst2 = sdarr[index].getStone2();
                            if (st.equals(testst1)) {
                                if (testst2.equals(origst2)) {
                                    flag = true;
                                    lgstones.add(st);
                                    break;
                                }
                            }
                            if (st.equals(testst2)) {
                                if (testst1.equals(origst1)) {
                                    flag = true;
                                    lgstones.add(st);
                                    break;
                                }
                            }
                            if (flag) {
                                lgstones.add(origst1);
                                lgstones.add(origst2);
                                flag = false;
                                if (checkEmptyPoints(cpts, lgstones, col, cpg1, cpg2)) {
                                    flag = true;
                                    break;
                                } else {
                                    flag = false;
                                    lgstones.clear();
                                }
                            }
                        }
                    }
                    index++;
                }
            }
        }
        return flag;
    }
