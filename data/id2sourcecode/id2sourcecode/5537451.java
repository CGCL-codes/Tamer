    protected void moveHandle(int sx, int sy) {
        double ox = ic.offScreenXD(sx);
        double oy = ic.offScreenYD(sy);
        x1d = x + x1R;
        y1d = y + y1R;
        x2d = x + x2R;
        y2d = y + y2R;
        double length = Math.sqrt((x2d - x1d) * (x2d - x1d) + (y2d - y1d) * (y2d - y1d));
        switch(activeHandle) {
            case 0:
                double dx = ox - x1d;
                double dy = oy - y1d;
                x1d = ox;
                y1d = oy;
                if (center) {
                    x2d -= dx;
                    y2d -= dy;
                }
                if (aspect) {
                    double ratio = length / (Math.sqrt((x2d - x1d) * (x2d - x1d) + (y2d - y1d) * (y2d - y1d)));
                    double xcd = x1d + (x2d - x1d) / 2;
                    double ycd = y1d + (y2d - y1d) / 2;
                    if (center) {
                        x1d = xcd - ratio * (xcd - x1d);
                        x2d = xcd + ratio * (x2d - xcd);
                        y1d = ycd - ratio * (ycd - y1d);
                        y2d = ycd + ratio * (y2d - ycd);
                    } else {
                        x1d = x2d - ratio * (x2d - x1d);
                        y1d = y2d - ratio * (y2d - y1d);
                    }
                }
                break;
            case 1:
                dx = ox - x2d;
                dy = oy - y2d;
                x2d = ox;
                y2d = oy;
                if (center) {
                    x1d -= dx;
                    y1d -= dy;
                }
                if (aspect) {
                    double ratio = length / (Math.sqrt((x2d - x1d) * (x2d - x1d) + (y2d - y1d) * (y2d - y1d)));
                    double xcd = x1d + (x2d - x1d) / 2;
                    double ycd = y1d + (y2d - y1d) / 2;
                    if (center) {
                        x1d = xcd - ratio * (xcd - x1d);
                        x2d = xcd + ratio * (x2d - xcd);
                        y1d = ycd - ratio * (ycd - y1d);
                        y2d = ycd + ratio * (y2d - ycd);
                    } else {
                        x2d = x1d + ratio * (x2d - x1d);
                        y2d = y1d + ratio * (y2d - y1d);
                    }
                }
                break;
            case 2:
                dx = ox - (x1d + (x2d - x1d) / 2);
                dy = oy - (y1d + (y2d - y1d) / 2);
                x1d += dx;
                y1d += dy;
                x2d += dx;
                y2d += dy;
                if (getStrokeWidth() > 1) {
                    x1d += xHandleOffset;
                    y1d += yHandleOffset;
                    x2d += xHandleOffset;
                    y2d += yHandleOffset;
                }
                break;
        }
        if (constrain) {
            double dx = Math.abs(x1d - x2d);
            double dy = Math.abs(y1d - y2d);
            double xcd = Math.min(x1d, x2d) + dx / 2;
            double ycd = Math.min(y1d, y2d) + dy / 2;
            if (activeHandle == 0) {
                if (dx >= dy) {
                    if (aspect) {
                        if (x2d > x1d) x1d = x2d - length; else x1d = x2d + length;
                    }
                    y1d = y2d;
                    if (center) {
                        y1d = y2d = ycd;
                        if (aspect) {
                            if (xcd > x1d) {
                                x1d = xcd - length / 2;
                                x2d = xcd + length / 2;
                            } else {
                                x1d = xcd + length / 2;
                                x2d = xcd - length / 2;
                            }
                        }
                    }
                } else {
                    if (aspect) {
                        if (y2d > y1d) y1d = y2d - length; else y1d = y2d + length;
                    }
                    x1d = x2d;
                    if (center) {
                        x1d = x2d = xcd;
                        if (aspect) {
                            if (ycd > y1d) {
                                y1d = ycd - length / 2;
                                y2d = ycd + length / 2;
                            } else {
                                y1d = ycd + length / 2;
                                y2d = ycd - length / 2;
                            }
                        }
                    }
                }
            } else if (activeHandle == 1) {
                if (dx >= dy) {
                    if (aspect) {
                        if (x1d > x2d) x2d = x1d - length; else x2d = x1d + length;
                    }
                    y2d = y1d;
                    if (center) {
                        y1d = y2d = ycd;
                        if (aspect) {
                            if (xcd > x1d) {
                                x1d = xcd - length / 2;
                                x2d = xcd + length / 2;
                            } else {
                                x1d = xcd + length / 2;
                                x2d = xcd - length / 2;
                            }
                        }
                    }
                } else {
                    if (aspect) {
                        if (y1d > y2d) y2d = y1d - length; else y2d = y1d + length;
                    }
                    x2d = x1d;
                    if (center) {
                        x1d = x2d = xcd;
                        if (aspect) {
                            if (ycd > y1d) {
                                y1d = ycd - length / 2;
                                y2d = ycd + length / 2;
                            } else {
                                y1d = ycd + length / 2;
                                y2d = ycd - length / 2;
                            }
                        }
                    }
                }
            }
        }
        x = (int) Math.min(x1d, x2d);
        y = (int) Math.min(y1d, y2d);
        x1R = x1d - x;
        y1R = y1d - y;
        x2R = x2d - x;
        y2R = y2d - y;
        width = (int) Math.abs(x2R - x1R);
        height = (int) Math.abs(y2R - y1R);
        updateClipRect();
        imp.draw(clipX, clipY, clipWidth, clipHeight);
        oldX = x;
        oldY = y;
        oldWidth = width;
        oldHeight = height;
    }
