    private int[] indexOf(C sc) {
        int[] ret = { 0, 0 };
        long[] x = { sc.starting(), sc.ending() };
        int mid, lb, ub;
        boolean[] done_x = { false, false };
        boolean mide, pullupperbound, pulllowerbound;
        if (DEBUG) System.out.println("                  inside indexOf");
        boolean cs1, cs2;
        for (int i = 0; i < 2; i++) {
            lb = 0;
            ub = length() * 2;
            do {
                mid = (lb + ub) / 2;
                mide = mid % 2 == 0;
                if (DEBUG) System.out.println();
                if (DEBUG) System.out.println("               mid=" + mid + " " + getOff(mid) + " " + getOff(mid + 1) + " i=" + i + "done i=" + done_x[i]);
                cs1 = cs2 = pulllowerbound = pullupperbound = false;
                if (i == 0) {
                    if (mide) {
                        if (getOff(mid) + 1 <= x[0]) cs1 = true;
                        if (x[0] <= getOff(mid + 1) + 1) cs2 = true;
                        if (cs1 && cs2) {
                            ret[0] = mid;
                            done_x[0] = true;
                        } else if (!cs1 && cs2) {
                            pulllowerbound = false;
                            pullupperbound = true;
                        } else if (cs1 && !cs2) {
                            pulllowerbound = true;
                            pullupperbound = false;
                        }
                    } else {
                        if (getOff(mid) + 2 <= x[0]) cs1 = true;
                        if (x[0] <= getOff(mid + 1)) cs2 = true;
                        if (cs1 && cs2) {
                            ret[0] = mid;
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
                        if (getOff(mid) - 1 <= x[1]) cs1 = true;
                        if (x[1] <= getOff(mid + 1) - 1) cs2 = true;
                        if (cs1 && cs2) {
                            ret[1] = mid;
                            done_x[1] = true;
                        } else if (!cs1 && cs2) {
                            pulllowerbound = false;
                            pullupperbound = true;
                        } else if (cs1 && !cs2) {
                            pulllowerbound = true;
                            pullupperbound = false;
                        }
                    } else {
                        if (getOff(mid) <= x[1]) cs1 = true;
                        if (x[1] <= getOff(mid + 1) - 2) cs2 = true;
                        if (cs1 && cs2) {
                            ret[1] = mid;
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
                if (DEBUG) System.out.println("              " + cs1 + " " + cs2 + " " + pullupperbound + " " + pulllowerbound);
                if (pullupperbound) ub = mid - 1;
                if (pulllowerbound) lb = mid + 1;
            } while (lb <= ub && !done_x[i]);
        }
        if (DEBUG) System.out.println("                done indexOf " + x[0] + " " + x[1]);
        if (DEBUG) System.out.println("ret=" + ret[0] + " " + ret[1]);
        return (ret);
    }
