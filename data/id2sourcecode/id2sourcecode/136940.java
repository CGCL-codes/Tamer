    private static int process(String line) {
        HashMap<String, String> isPalindrome = new HashMap<String, String>();
        for (int k = 0; k < line.length(); k++) {
            for (int j = k + 1; j <= line.length(); j++) {
                String sub = line.substring(k, j);
                if (isPalindrome.get(sub) == null) {
                    if (isPalindrome(sub)) {
                        isPalindrome.put(sub, sub);
                    }
                }
            }
        }
        return isPalindrome.size();
    }
