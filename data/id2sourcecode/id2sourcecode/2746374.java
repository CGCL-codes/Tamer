        public boolean isLeftRotated() {
            double angle = (startAngle + endAngle) / 2;
            int rotations = 0;
            if (angle < 0) {
                rotations = 1 + (int) ((-angle) / (2 * Math.PI));
            } else {
                rotations = -((int) (angle / (2 * Math.PI)));
            }
            angle += (2 * Math.PI) * rotations;
            isLeft = ((angle >= (0.5 * Math.PI)) && (angle <= (1.5 * Math.PI)));
            return isLeft;
        }
