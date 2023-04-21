    private static void process(String line) {
        StringBuffer sb = new StringBuffer();
        for (int k = 0; k < line.length(); k++) {
            char c = line.charAt(k);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                if ((c >= 'a' && c <= 'z')) {
                    int n = c - 'a';
                    c = (char) (n + 'A');
                }
                sb.append(c);
            }
        }
        if (isPalindrome(sb.toString())) {
            System.out.println("You won't be eaten!");
        } else {
            System.out.println("Uh oh..");
        }
    }
