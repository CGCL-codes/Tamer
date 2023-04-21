package transformaciones_cg;

import napplet.NApplet;
import processing.core.PApplet;

/**
 *
 * @author Carlos
 */
public class PuntoMedioElipse1 extends NApplet {

    int x1 = 0;

    int y1 = 0;

    int x2 = 0;

    int y2 = 0;

    int radio;

    public void setup() {
        background(255);
        size(1024, 800);
        textSize(12);
        textAlign(LEFT);
    }

    public void draw() {
        background(255);
        fill(0, 102, 153);
        text("X1: " + x1 + "\n", 5, 10);
        text("Y2: " + y1, 5, 20);
        text("X2: " + mouseX + "\n", 5, 30);
        text("Y2: " + mouseY, 5, 40);
        text("R: " + radio, 5, 50);
        radio = round(sqrt((abs(x2 - x1) * abs(x2 - x1)) + (abs(y2 - y1) * abs(y2 - y1))));
        if (mousePressed) {
            x2 = mouseX;
            y2 = mouseY;
            if (x1 != x2 && y1 != y2) {
                ellipseMidPoint(x1, y1, abs(x2 - x1), abs(y2 - y1));
                pintarLinea();
            }
        }
    }

    public void mouseReleased() {
        ellipseMidPoint(x1, y1, abs(x2 - x1), abs(y2 - y1));
        x2 = 0;
        y2 = 0;
        y1 = 0;
        x1 = 0;
    }

    public void mousePressed() {
        x1 = mouseX;
        y1 = mouseY;
    }

    void pintarLinea() {
        stroke(25, 100, 100);
        int x, y, fin, incremento = 0, decision, dx = abs(x2 - x1), dy = abs(y2 - y1);
        if (dx > dy) {
            decision = 2 * dy - dx;
            if (x1 < x2) {
                x = x1;
                y = y1;
                fin = x2;
                if (y1 < y2) {
                    incremento = 1;
                }
                if (y1 > y2) {
                    incremento = -1;
                }
            } else {
                x = x2;
                y = y2;
                fin = x1;
                if (y2 < y1) {
                    incremento = 1;
                }
                if (y2 > y1) {
                    incremento = -1;
                }
            }
            while (x <= fin) {
                point(x, y);
                if (decision < 0) {
                    decision = decision + 2 * dy;
                } else {
                    y = y + incremento;
                    decision = decision + 2 * (dy - dx);
                }
                x++;
            }
        } else {
            decision = 2 * dx - dy;
            if (y1 < y2) {
                y = y1;
                x = x1;
                fin = y2;
                if (x1 < x2) {
                    incremento = 1;
                }
                if (x1 > x2) {
                    incremento = -1;
                }
            } else {
                y = y2;
                x = x2;
                fin = y1;
                if (x2 < x1) {
                    incremento = 1;
                }
                if (x2 > x1) {
                    incremento = -1;
                }
            }
            while (y <= fin) {
                point(x, y);
                if (decision < 0) {
                    decision = decision + 2 * dx;
                } else {
                    x = x + incremento;
                    decision = decision + 2 * (dx - dy);
                }
                y++;
            }
        }
    }

    void PlotPoint(int xc, int yc, int x, int y) {
        point(xc + x, yc - y);
        point(xc - x, yc - y);
        point(xc - x, yc + y);
        point(xc + x, yc + y);
    }

    void ellipseMidPoint(int xc, int yc, int rx, int ry) {
        int x, y, p, px, py;
        int rx2, ry2, dosRx2, dosRy2;
        ry2 = ry * ry;
        rx2 = rx * rx;
        dosRy2 = 2 * ry2;
        dosRx2 = 2 * rx2;
        x = 0;
        y = ry;
        PlotPoint(xc, yc, x, y);
        p = (int) (ry2 - rx2 * ry + (0.25 * rx2));
        px = 0;
        py = dosRx2 * y;
        while (px < py) {
            x = x + 1;
            px = px + dosRy2;
            if (p < 0) {
                p = p + ry2 + px;
            } else {
                y = y - 1;
                py = py - dosRx2;
                p = p + ry2 + px - py;
            }
            PlotPoint(xc, yc, x, y);
        }
        p = (int) (ry2 * (x + 0.5) * (x + 0.5) + rx2 * (y - 1) * (y - 1) - rx2 * ry2);
        px = 0;
        py = dosRx2 * y;
        while (y > 0) {
            y = y - 1;
            py = py - dosRx2;
            if (p > 0) {
                p = p + rx2 - py;
            } else {
                x = x + 1;
                px = px + dosRy2;
                p = p + rx2 - py + px;
            }
            PlotPoint(xc, yc, x, y);
        }
    }
}
