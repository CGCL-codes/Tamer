    public static int pickFromDistribution(final Object[] objs, final RandomChoiceChooser chooser, final float prob, final int checkboundary) {
        if (prob < 0.0f || prob > 1.0f) throw new ArithmeticException("Invalid probability for pickFromDistribution (must be 0.0<=x<=1.0)");
        if (objs.length == 1) return 0; else if (objs.length < checkboundary) {
            for (int x = 0; x < objs.length - 1; x++) if (chooser.getProbability(objs[x]) > prob) return exemptZeroes(objs, chooser, x);
            return exemptZeroes(objs, chooser, objs.length - 1);
        } else {
            int top = objs.length - 1;
            int bottom = 0;
            int cur;
            while (top != bottom) {
                cur = (top + bottom) / 2;
                if (chooser.getProbability(objs[cur]) > prob) if (cur == 0 || chooser.getProbability(objs[cur - 1]) <= prob) return exemptZeroes(objs, chooser, cur); else top = cur; else if (cur == objs.length - 1) return exemptZeroes(objs, chooser, cur); else if (bottom == cur) bottom++; else bottom = cur;
            }
            return exemptZeroes(objs, chooser, bottom);
        }
    }
