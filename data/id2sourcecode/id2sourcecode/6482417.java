    private static String calc(String exp) {
        if (exp.startsWith("random")) {
            int beg = exp.indexOf("(");
            int end = exp.indexOf(")");
            String[] ss = exp.substring(beg + 1, end).split(",");
            int rb = Integer.parseInt(ss[0]);
            int re = Integer.parseInt(ss[1]);
            int rr = rb + random.nextInt(re - rb);
            return String.valueOf(rr);
        }
        return exp;
    }
