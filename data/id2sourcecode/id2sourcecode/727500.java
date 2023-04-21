    public static void main(String args[]) {
        try {
            DB.Initialize();
            Random r = new Random();
            StudentInfo[] s = StudentModel.getStudents();
            Club[] c = ClubModel.getClubs();
            if (false) {
                for (Club b : c) {
                    if (b.rank.equals("-1")) {
                        b.rank = "" + r.nextInt(11);
                        System.out.println(b.name + ": -1 -> " + b.rank + "  " + DB.Update("UPDATE Club SET rank='" + b.rank + "' WHERE club_id=" + b.id));
                    }
                    int rank = Integer.parseInt(b.rank);
                    String q = "20101";
                    double moneyz[] = { 200, 300, 500, 600, 800, 1000, 2000, 4000, 6000, 8000, 10000 };
                    System.out.println("Budget: " + moneyz[rank] + "   " + DB.Update("INSERT INTO Budget (`club_id`, `quarter_id`, `used`, `available`) VALUES ( " + b.id + ", " + q + ", 0, " + moneyz[rank] + " )") + "   " + b.name);
                }
            }
            int max_club = 3;
            if (false) {
                for (StudentInfo i : s) {
                    int num = r.nextInt(max_club + 1);
                    num -= ClubModel.getClubsOfMember(i.uid).length;
                    for (int k = 0; k < num; k++) {
                        int join = r.nextInt(c.length);
                        System.out.println(i.uid + " -> " + c[join].name + "   " + ClubModel.addMember(i, c[join]));
                    }
                }
            }
        } catch (DBError de) {
        }
    }
