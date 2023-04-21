    private IndexAndDissolvabiltyWrapper indexOf(long x[], DType d, boolean ver) {
        IndexAndDissolvabiltyWrapper ret = new IndexAndDissolvabiltyWrapper();
        int mid, lb, ub;
        boolean[] done_x = { false, false };
        boolean mide, pullupperbound, pulllowerbound;
        System.out.println("                  inside indexOf");
        boolean cs1, cs2;
        for (int i = 0; i < 2; i++) {
            lb = 0;
            ub = dataAreaLength;
            do {
                mid = (lb + ub) / 2;
                mide = mid % 2 == 0;
                System.out.println();
                System.out.println("               mid=" + mid + " " + off[mid] + " " + off[mid + 1] + " i=" + i + "done i=" + done_x[i]);
                cs1 = cs2 = pulllowerbound = pullupperbound = false;
                if (i == 0) {
                    if (mide) {
                        if (off[mid] + 1 <= x[0]) cs1 = true;
                        if (x[0] <= off[mid + 1] + 1) cs2 = true;
                        if (cs1 && cs2) {
                            ret.x1 = mid;
                            done_x[0] = true;
                        } else if (!cs1 && cs2) {
                            pulllowerbound = false;
                            pullupperbound = true;
                        } else if (cs1 && !cs2) {
                            pulllowerbound = true;
                            pullupperbound = false;
                        }
                    } else {
                        if (off[mid] + 2 <= x[0]) cs1 = true;
                        if (x[0] <= off[mid + 1]) cs2 = true;
                        if (cs1 && cs2) {
                            ret.x1 = mid;
                            done_x[0] = true;
                        } else if (!cs1 && cs2) {
                            pulllowerbound = false;
                            pullupperbound = true;
                        } else if (cs1 && !cs2) {
                            pulllowerbound = true;
                            pullupperbound = false;
                        }
                    }
                } else {
                    if (mide) {
                        if (off[mid] - 1 <= x[1]) cs1 = true;
                        if (x[1] <= off[mid + 1] - 1) cs2 = true;
                        if (cs1 && cs2) {
                            ret.x2 = mid;
                            done_x[1] = true;
                        } else if (!cs1 && cs2) {
                            pulllowerbound = false;
                            pullupperbound = true;
                        } else if (cs1 && !cs2) {
                            pulllowerbound = true;
                            pullupperbound = false;
                        }
                    } else {
                        if (off[mid] <= x[1]) cs1 = true;
                        if (x[1] <= off[mid + 1] - 2) cs2 = true;
                        if (cs1 && cs2) {
                            ret.x2 = mid;
                            done_x[1] = true;
                        } else if (!cs1 && cs2) {
                            pulllowerbound = false;
                            pullupperbound = true;
                        } else if (cs1 && !cs2) {
                            pulllowerbound = true;
                            pullupperbound = false;
                        }
                    }
                }
                System.out.println("              " + cs1 + " " + cs2 + " " + pullupperbound + " " + pulllowerbound);
                if (pullupperbound) ub = mid - 1;
                if (pulllowerbound) lb = mid + 1;
            } while (lb <= ub && !done_x[i]);
        }
        System.out.println("                done indexOf " + x[0] + " " + x[1]);
        System.out.println("ret=" + ret.x1 + " " + ret.x2);
        ret.dx1 = isSame(ret.x1, d, ver);
        ret.dx2 = isSame(ret.x2, d, ver);
        return (ret);
    }
