    public Object next(int index) {
        if (index >= N - 2) next(); else {
            int m = Al[index];
            sort(index + 1, N - 1);
            if (m > Al[N - 1]) {
                if (index > 0) return next(index - 1);
                first();
            } else {
                int o;
                for (o = N - 2; Al[o] > m; o--) ;
                Al[index] = Al[o + 1];
                Al[o + 1] = m;
            }
        }
        return this;
    }
