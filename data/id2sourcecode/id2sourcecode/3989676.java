    public void paint(Graphics g) {
        boolean dashed = true;
        Color old = this.color;
        Point sp = this.getStartPoint();
        Point ep = this.getEndPoint();
        if (this.initstart) {
            this.initVector(this.pointstack);
        }
        this.initstart = false;
        if (sp != null && ep != null) {
            if (this.vector.size() >= 3) {
                while (true) {
                    if (this.vector.size() >= 3) {
                        int d = -1;
                        int m = 0;
                        int size = this.vector.size() - 1;
                        for (m = 0; m < size; m++) {
                            Point obj1 = (Point) this.vector.elementAt(m);
                            Point obj2 = (Point) this.vector.elementAt(m + 1);
                            if (m == 0) {
                                obj1 = this.getStartPoint();
                            }
                            if (m == this.vector.size() - 2) {
                                obj2 = this.getEndPoint();
                            }
                            d = this.getDistance(obj1, obj2);
                            if (d <= 10) {
                                if (m == this.vector.size() - 2) {
                                    this.vector.removeElementAt(m);
                                } else {
                                    this.vector.removeElementAt(m + 1);
                                }
                                break;
                            }
                        }
                        if (m == size) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                while (true) {
                    if (this.vector.size() >= 3) {
                        boolean remove = false;
                        int n = 0;
                        int size = this.vector.size() - 2;
                        for (n = 0; n < size; n++) {
                            Point obj1 = (Point) this.vector.elementAt(n);
                            Point obj2 = (Point) this.vector.elementAt(n + 1);
                            Point obj3 = (Point) this.vector.elementAt(n + 2);
                            if (n == 0) {
                                obj1 = this.getStartPoint();
                            }
                            if (n == this.vector.size() - 3) {
                                obj3 = this.getEndPoint();
                            }
                            remove = this.lineTolineAngle(obj1, obj2, obj3);
                            if (remove) {
                                this.vector.removeElementAt(n + 1);
                                break;
                            }
                        }
                        if (n == size) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            int i = 0, x1 = 0, y1 = 0, x2 = 0, y2 = 0, d2 = 0, h2 = 0;
            int mx = 0, my = 0;
            int hx = 0, hy = 0, ex1 = 0, ey1 = 0, ex2 = 0, ey2 = 0;
            double k1 = 0, k2 = 0;
            double sina = 0, cosa = 0;
            double sinb = 0, cosb = 0;
            double tx = 0, ty = 0;
            boolean moveline = false;
            x1 = ep.x;
            y1 = ep.y;
            x2 = sp.x;
            y2 = sp.y;
            if (this.ispassed) {
                g.setColor(color.green);
            } else if (this.currentselect) {
                this.color = this.DEF_SELECTEDCOLOR;
                g.setColor(this.color);
                this.currentselect = false;
            } else if (isCurrentToEdit()) {
                this.color = this.DEF_CURREDITCOLOR;
                g.setColor(this.color);
            } else {
                this.color = this.DEF_COLOR;
                g.setColor(this.color);
            }
            if (this.vector.size() < 2) {
                d2 = 0;
                h2 = 0;
            } else {
                Node node = (Node) this.getEndnode();
                d2 = node.getRect().width;
                h2 = node.getRect().height;
            }
            Point arrowhead = null;
            arrowhead = this.getArrowhead(new Point(x2, y2), new Point(x1, y1), d2, h2);
            if (this.vector.size() < 3) {
                if (this._movepoint != null) {
                    drawLine(g, x2, y2, this._movepoint.x, this._movepoint.y, dashed);
                    x2 = this._movepoint.x;
                    y2 = this._movepoint.y;
                    arrowhead = this.getArrowhead(new Point(x2, y2), new Point(x1, y1), d2, h2);
                    drawLine(g, this._movepoint.x, this._movepoint.y, arrowhead.x, arrowhead.y, dashed);
                    this._movepoint = null;
                } else {
                    drawLine(g, x2, y2, arrowhead.x, arrowhead.y, dashed);
                }
            } else {
                if (this._movepoint != null) {
                    int whichLine = this.getWhichLine(this.getBreakpoint());
                    for (int j = 0; j < this.vector.size() - 1; j++) {
                        Point obj1 = (Point) this.vector.elementAt(j);
                        Point obj2 = (Point) this.vector.elementAt(j + 1);
                        x2 = obj1.x;
                        y2 = obj1.y;
                        if (j == 0) {
                            obj1 = this.getStartPoint();
                        }
                        if (j == this.vector.size() - 2) {
                            arrowhead = this.getArrowhead(new Point(x2, y2), new Point(x1, y1), d2, h2);
                            obj2 = arrowhead;
                        }
                        if (j == whichLine) {
                            g.drawLine(obj1.x, obj1.y, this._movepoint.x, this._movepoint.y);
                            x2 = this._movepoint.x;
                            y2 = this._movepoint.y;
                            if (j == this.vector.size() - 2) {
                                arrowhead = this.getArrowhead(new Point(x2, y2), new Point(x1, y1), d2, h2);
                                obj2 = arrowhead;
                            }
                            drawLine(g, this._movepoint.x, this._movepoint.y, obj2.x, obj2.y, dashed);
                        } else {
                            g.drawLine(obj1.x, obj1.y, obj2.x, obj2.y);
                        }
                    }
                    this._movepoint = null;
                } else {
                    for (int k = 0; k < this.vector.size() - 1; k++) {
                        Point obj3 = (Point) this.vector.elementAt(k);
                        Point obj4 = (Point) this.vector.elementAt(k + 1);
                        x2 = obj3.x;
                        y2 = obj3.y;
                        if (k == 0) {
                            obj3 = this.getStartPoint();
                        }
                        if (k == this.vector.size() - 2) {
                            arrowhead = this.getArrowhead(new Point(x2, y2), new Point(x1, y1), d2, h2);
                            obj4 = arrowhead;
                        }
                        drawLine(g, obj3.x, obj3.y, obj4.x, obj4.y, dashed);
                    }
                }
            }
            mx = (x2 + x1) / 2;
            my = (y2 + y1) / 2;
            arrowhead = this.getArrowhead(new Point(x2, y2), new Point(x1, y1), d2, h2);
            sina = Math.abs((double) Math.sqrt((y2 - y1) * (y2 - y1)) / Math.sqrt(((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))));
            cosa = Math.abs((double) Math.sqrt((x2 - x1) * (x2 - x1)) / Math.sqrt(((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))));
            g.setColor(Color.black);
            if (this.name != null) {
                java.awt.FontMetrics fm = _owner.getFontMetrics(font);
                int rx = mx - 10;
                int ry = my + fm.getHeight();
                g.setColor(Color.blue);
                g.drawString(name, rx, ry);
                g.setColor(Color.black);
            }
        }
        this.color = old;
        this.setPointStack(this.vector);
    }
