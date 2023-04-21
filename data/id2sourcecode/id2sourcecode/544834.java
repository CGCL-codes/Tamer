    int scoreStroke(Vector xv, Vector yv, int begi, int endi, String dir, int depth) {
        if (dir.length() == 1) {
            int i;
            int difx, dify;
            difx = ((Integer) xv.elementAt(endi - 1)).intValue() - ((Integer) xv.elementAt(begi)).intValue();
            dify = ((Integer) yv.elementAt(endi - 1)).intValue() - ((Integer) yv.elementAt(begi)).intValue();
            if ((difx == 0) && (dify == 0)) {
                return (hugeCost);
            }
            if ((difx * difx + dify * dify > (20 * 20)) && (endi - begi > 5) && (depth < 4)) {
                int mi = (endi + begi) / 2;
                int cost1, cost2;
                cost1 = scoreStroke(xv, yv, begi, mi, dir, depth + 1);
                cost2 = scoreStroke(xv, yv, mi, endi, dir, depth + 1);
                return ((cost1 + cost2) / 2);
            }
            double ang;
            ang = Math.atan2(-dify, difx);
            double myang;
            switch(dir.charAt(0)) {
                case '6':
                    myang = 0;
                    break;
                case '9':
                    myang = Math.PI / 4;
                    break;
                case '8':
                    myang = Math.PI / 2;
                    break;
                case '7':
                    myang = Math.PI * 3 / 4;
                    break;
                case '4':
                    myang = Math.PI;
                    break;
                case '3':
                    myang = -Math.PI / 4;
                    break;
                case '2':
                    myang = -Math.PI / 2;
                    break;
                case '1':
                    myang = -Math.PI * 3 / 4;
                    break;
                default:
                    System.out.println("Illegal char");
                    myang = 0;
            }
            double difang = myang - ang;
            while (difang < 0) difang += 2 * Math.PI;
            while (difang > 2 * Math.PI) difang -= 2 * Math.PI;
            if (difang > Math.PI) difang = 2 * Math.PI - difang;
            int retcost = (int) Math.round(difang * angScale) + sCost;
            return (retcost);
        } else if (begi == endi) {
            return (hugeCost * dir.length());
        } else {
            int l1, l2;
            l1 = dir.length() / 2;
            l2 = dir.length() - l1;
            String s1, s2;
            s1 = dir.substring(0, l1);
            s2 = dir.substring(l1, dir.length());
            int i;
            int mincost = hugeCost * dir.length() * 2;
            int s1l = s1.length();
            int s2l = s2.length();
            int step = (endi - begi) / 10;
            if (step < 1) step = 1;
            for (i = begi + 1 + s1l; i < endi - 1 - s2l; i += step) {
                int ncost;
                ncost = scoreStroke(xv, yv, begi, i + 1, s1, depth) + scoreStroke(xv, yv, i - 1, endi, s2, depth);
                if (ncost < mincost) mincost = ncost;
            }
            return (mincost);
        }
    }
