        double pop() {
            double result = r[0];
            for (int i = 0; i < 3; i++) {
                r[i] = r[i + 1];
            }
            return result;
        }
