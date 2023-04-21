    public static Calendar[] parseTimeSeg(String msg) {
        Calendar[] cals = null;
        if (msg != null && msg.length() > 1) {
            String stime = null;
            String etime = null;
            msg = GFString.treatChineseNumber(msg, ft2);
            String[] atoms = new String[msg.length()];
            for (int i = 0; i < msg.length(); i++) {
                if (i < msg.length() - 1) atoms[i] = msg.substring(i, i + 1); else atoms[i] = msg.substring(i);
            }
            String tk = "";
            String tk2 = null;
            String tk3 = null;
            String tk4 = null;
            for (int i = 0; i < atoms.length; i++) {
                for (int j = i; j < atoms.length; j++) {
                    if (isTimeKey(atoms[j])) {
                        tk += atoms[j];
                        tk2 = j < atoms.length - 1 ? atoms[j + 1] : null;
                        tk4 = j < atoms.length - 2 ? atoms[j + 2] : null;
                        if (isDateTime(tk) && (!isTimeKey(tk2) || !isDateTime(tk + tk2) && !isTimeKey(tk4))) {
                            tk3 = splitTime(tk);
                            if (tk3 != null) {
                                if (stime == null) stime = tk3; else if (etime == null && stime.indexOf("/t3") == stime.lastIndexOf("/t3")) etime = tk3;
                            }
                            i = j + 1;
                            tk = "";
                        } else if (!isTimeKey(tk2)) tk = "";
                    }
                }
            }
            System.out.println("stime:" + stime);
            System.out.println("etime:" + etime);
            cals = new Calendar[2];
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal2.set(2100, 0, 0, 0, 0, 0);
            int[][] rs1 = null;
            int[][] rs2 = null;
            if (stime != null && stime.length() > 1) {
                int index1 = stime.indexOf("/t3");
                int index2 = stime.lastIndexOf("/t3");
                int index3 = stime.indexOf("/t4");
                if (index1 > 1 && index1 == index2) {
                    if ("����".equals(stime.substring(index1 - 2, index1)) || "ǰ��".equals(stime.substring(index1 - 2, index1))) {
                        rs1 = parseTime(stime);
                        if (rs1[0][0] == 0) rs1[0][0] = cal1.get(Calendar.YEAR);
                        if (rs1[0][1] == 0) rs1[0][1] = cal1.get(Calendar.MONTH);
                        if (rs1[0][3] == 0 && rs1[0][2] == 0) rs1[0][3] = cal1.get(Calendar.HOUR_OF_DAY);
                        if (rs1[0][4] == 0 && rs1[0][2] == 0 && rs1[0][3] == 0) rs1[0][4] = cal1.get(Calendar.MINUTE);
                        if (rs1[0][2] == 0) rs1[0][2] = cal1.get(Calendar.DAY_OF_MONTH);
                        if (rs1[1][0] == 0) rs1[1][0] = rs1[0][0];
                        if (rs1[1][1] == 0) rs1[1][1] = rs1[0][1];
                        if (rs1[1][2] == 0) rs1[1][2] = rs1[0][2];
                        if (rs1[1][3] == 0) rs1[1][3] = rs1[0][3];
                        if (rs1[1][4] == 0) rs1[1][4] = rs1[0][4];
                        cal1.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
                        cal2.set(rs1[1][0], rs1[1][1], rs1[1][2], rs1[1][3], rs1[1][4], rs1[1][5]);
                    } else if ("��ǰ".equals(stime.substring(index1 - 2, index1)) || "֮ǰ".equals(stime.substring(index1 - 2, index1)) || "ǰ".equals(stime.substring(index1 - 1, index1))) {
                        if (stime.indexOf(" ǰ/t3") != -1) rs1 = parseTime(stime.substring(0, index1 - 2)); else rs1 = parseTime(stime.substring(0, index1 - 3));
                        if (rs1[0][0] == 0) rs1[0][0] = cal1.get(Calendar.YEAR);
                        if (rs1[0][1] == 0) rs1[0][1] = cal1.get(Calendar.MONTH);
                        if (rs1[0][3] == 0 && rs1[0][2] == 0) rs1[0][3] = cal1.get(Calendar.HOUR_OF_DAY);
                        if (rs1[0][4] == 0 && rs1[0][2] == 0 && rs1[0][3] == 0) rs1[0][4] = cal1.get(Calendar.MINUTE);
                        if (rs1[0][2] == 0) rs1[0][2] = cal1.get(Calendar.DAY_OF_MONTH);
                        cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DAY_OF_MONTH), cal1.get(Calendar.HOUR_OF_DAY), cal1.get(Calendar.MINUTE), cal1.get(Calendar.SECOND));
                        cal2.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
                    } else if ("�Ժ�".equals(stime.substring(index1 - 2, index1)) || "֮��".equals(stime.substring(index1 - 2, index1)) || "��".equals(stime.substring(index1 - 1, index1))) {
                        if (stime.indexOf(" ��/t3") != -1) rs1 = parseTime(stime.substring(0, index1 - 2)); else rs1 = parseTime(stime.substring(0, index1 - 3));
                        if (rs1[0][0] == 0) rs1[0][0] = cal1.get(Calendar.YEAR);
                        if (rs1[0][1] == 0) rs1[0][1] = cal1.get(Calendar.MONTH);
                        if (rs1[0][3] == 0 && rs1[0][2] == 0) rs1[0][3] = cal1.get(Calendar.HOUR_OF_DAY);
                        if (rs1[0][4] == 0 && rs1[0][2] == 0 && rs1[0][3] == 0) rs1[0][4] = cal1.get(Calendar.MINUTE);
                        if (rs1[0][2] == 0) rs1[0][2] = cal1.get(Calendar.DAY_OF_MONTH);
                        if (rs1[0][3] > 0 || rs1[0][4] > 0) rs1[1][2] = rs1[0][2]; else {
                            Calendar calt = Calendar.getInstance();
                            calt.set(rs1[0][0], rs1[0][1], 0, 0, 0, 0);
                            rs1[1][2] = getLastDayOfMonth(calt);
                        }
                        cal1.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
                        cal2.set(rs1[0][0], rs1[0][1], rs1[1][2], 23, 59, 0);
                    }
                } else if (index1 > 1 && index2 > index1) {
                    rs1 = parseTime(stime.substring(0, index1 - 3));
                    rs2 = parseTime(stime.substring(index1 + 4, index2 - 3));
                    if (rs1[0][0] == 0) rs1[0][0] = cal1.get(Calendar.YEAR);
                    if (rs1[0][1] == 0) rs1[0][1] = cal1.get(Calendar.MONTH);
                    if (rs2[0][0] == 0) rs2[0][0] = rs1[0][0];
                    if (rs2[0][1] == 0) rs2[0][1] = rs1[0][1];
                    if (rs2[0][2] == 0) rs2[0][2] = rs1[0][2];
                    if (rs2[0][3] == 0) rs2[0][3] = rs1[0][3];
                    if (rs1[0][3] > 12 && rs2[0][3] <= 12) rs2[0][3] += 12;
                    cal1.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
                    cal2.set(rs2[0][0], rs2[0][1], rs2[0][2], rs2[0][3], rs2[0][4], rs2[0][5]);
                } else if (index3 > 1) {
                    rs1 = parseTime(stime.substring(0, index3 - 2));
                    rs2 = parseTime(stime.substring(index3 + 3));
                    if (rs1[0][0] == 0) rs1[0][0] = cal1.get(Calendar.YEAR);
                    if (rs1[0][1] == 0) rs1[0][1] = cal1.get(Calendar.MONTH);
                    if (rs1[0][3] == 0 && rs1[0][2] == 0) rs1[0][3] = cal1.get(Calendar.HOUR_OF_DAY);
                    if (rs1[0][4] == 0 && rs1[0][2] == 0 && rs1[0][3] == 0) rs1[0][4] = cal1.get(Calendar.MINUTE);
                    if (rs1[0][2] == 0) rs1[0][2] = cal1.get(Calendar.DAY_OF_MONTH);
                    if (rs2[0][0] == 0) rs2[0][0] = rs1[0][0];
                    if (rs2[0][1] == 0) rs2[0][1] = rs1[0][1];
                    if (rs2[0][2] == 0) rs2[0][2] = rs1[0][2];
                    if (rs2[0][3] == 0 && rs2[0][4] == 0) rs2[0][4] = 59;
                    if (rs2[0][3] == 0) rs2[0][3] = 23;
                    if (rs1[0][3] > 12 && rs2[0][3] < 12) rs2[0][3] += 12;
                    cal1.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
                    cal2.set(rs2[0][0], rs2[0][1], rs2[0][2], rs2[0][3], rs2[0][4], rs2[0][5]);
                } else {
                    rs1 = parseTime(stime);
                    if (rs1[0][0] == 0) rs1[0][0] = cal1.get(Calendar.YEAR);
                    if (rs1[0][1] == 0) rs1[0][1] = cal1.get(Calendar.MONTH);
                    if (rs1[0][3] == 0 && rs1[0][2] == 0) rs1[0][3] = cal1.get(Calendar.HOUR_OF_DAY);
                    if (rs1[0][4] == 0 && rs1[0][2] == 0 && rs1[0][3] == 0) rs1[0][4] = cal1.get(Calendar.MINUTE);
                    if (rs1[0][2] == 0) rs1[0][2] = cal1.get(Calendar.DAY_OF_MONTH);
                    if (etime != null && etime.length() > 1) {
                        rs2 = parseTime(etime);
                        if (rs2[0][0] == 0) rs2[0][0] = rs1[0][0];
                        if (rs2[0][1] == 0) rs2[0][1] = rs1[0][1];
                        if (rs2[0][2] == 0) rs2[0][2] = rs1[0][2];
                        if (rs2[0][3] == 0) rs2[0][3] = rs1[0][3];
                        cal1.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
                        cal2.set(rs2[0][0], rs2[0][1], rs2[0][2], rs2[0][3], rs2[0][4], rs2[0][5]);
                    } else {
                        if (rs1[1][0] == 0) rs1[1][0] = rs1[0][0];
                        if (rs1[1][1] == 0) rs1[1][1] = rs1[0][1];
                        if (rs1[1][2] == 0) rs1[1][2] = rs1[0][2];
                        if (rs1[1][3] == 0) rs1[1][3] = rs1[0][3];
                        if (rs1[1][4] == 0) rs1[1][4] = rs1[0][4];
                        cal1.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
                        cal2.set(rs1[1][0], rs1[1][1], rs1[1][2], rs1[1][3], rs1[1][4], rs1[1][5]);
                    }
                }
            }
            cals[0] = cal1;
            cals[1] = cal2;
            System.out.println("cal1:" + cdate(cal1, "yyyy-mm-dd hh24:mi:ss"));
            System.out.println("cal2:" + cdate(cal2, "yyyy-mm-dd hh24:mi:ss"));
        }
        return cals;
    }
