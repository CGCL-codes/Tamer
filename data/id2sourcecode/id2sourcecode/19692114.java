    protected int slideWindow(StringBuffer s) {
        StringBuffer r = new StringBuffer(s);
        r.reverse();
        int max_count = 0;
        int max_i = 0;
        int max_j = 0;
        for (int i = 0; i < s.length(); i++) {
            int counter = 0;
            for (int j = i; j < r.length(); j++) {
                if (s.charAt(j) == Statics.getAcidComp(r.charAt(j))) {
                    counter++;
                } else {
                    if (counter > max_count) {
                        max_count = counter;
                        max_i = j - counter;
                        max_j = j;
                    }
                    counter = 0;
                }
            }
        }
        if (max_count > 9999) {
            System.out.println(s);
            for (int i = 0; i < r.length(); i++) {
                System.out.print(Statics.getAcidComp(r.charAt(i)));
            }
            System.out.print("\n");
            if ((max_i + max_j) == s.length()) {
                System.out.println("Is Palindrome");
            }
            System.out.println("Max Count: " + max_count);
            System.out.println("Start: " + max_i);
            System.out.println("End: " + max_j);
        }
        return max_count;
    }
