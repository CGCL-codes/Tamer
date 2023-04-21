        void set(BasicEvent a, BasicEvent b) {
            this.timestamp = b.timestamp;
            ax = a.x;
            bx = b.x;
            ay = a.y;
            by = b.y;
            x = (ax + bx) / 2;
            y = (ay + by) / 2;
            dt = b.timestamp - a.timestamp;
            dx = ax - bx;
            dy = ay - by;
            length = Math.sqrt(dx * dx + dy * dy);
            thetaRad = Math.atan2(dx, -dy);
            rhoPixels = x * Math.cos(thetaRad) + y * Math.sin(thetaRad);
            if (rhoPixels < 0) {
                rhoPixels = -rhoPixels;
                if (thetaRad > 0) {
                    thetaRad -= Math.PI;
                } else {
                    thetaRad += Math.PI;
                }
            }
        }
