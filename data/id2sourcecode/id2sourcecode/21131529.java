        public Pair[] getBrackets() {
            if (sA == null) {
                return null;
            }
            if (variable == null) {
                return null;
            }
            double xa;
            double xb;
            double x = variable.getValue();
            final double xl = variable.getLowerLimit();
            final double xu = variable.getUpperLimit();
            final double dx = variable.getStep();
            if (debug) {
                System.out.println("bracketFinder::getBrackets(), x(init) = " + x);
            }
            final double windowRatio = 0.1;
            if ((xl <= x) && (x <= xu)) {
                if (dx != 0) {
                    xa = x + dx;
                    xa = checkLimits(xa);
                    xb = x;
                } else {
                    if ((xl != (-Double.MAX_VALUE)) && (xu != Double.MAX_VALUE)) {
                        xa = x + (xu - xl) * windowRatio;
                        xa = checkLimits(xa);
                        xb = x;
                    } else {
                        xa = x + 0.1;
                        xa = checkLimits(xa);
                        xb = x;
                    }
                }
            } else {
                if ((xl != (-Double.MAX_VALUE)) && (xu != Double.MAX_VALUE)) {
                    xa = (xl + xu) / 2 + (xu - xl) * windowRatio;
                    xb = (xl + xu) / 2;
                } else {
                    xa = 0.1;
                    xb = 0;
                }
            }
            return getBrackets(xa, xb);
        }
