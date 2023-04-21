        public void clamp() {
            if (value < minval) {
                value = minval + (minval - value);
                if (value > maxval) {
                    value = (minval + maxval) / 2;
                }
                step = rand(minstep, maxstep);
            } else if (value > maxval) {
                value = maxval - (value - maxval);
                if (value < minval) {
                    value = (minval + maxval) / 2;
                }
                step = -rand(minstep, maxstep);
            }
        }
