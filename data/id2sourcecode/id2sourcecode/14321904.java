    public static boolean palindrome(int input) {
        String inputAsStreing = "" + input;
        int stringLength = inputAsStreing.length();
        for (int i = 0; i < stringLength / 2; i++) {
            if (inputAsStreing.charAt(i) != inputAsStreing.charAt(stringLength - 1 - i)) return false;
        }
        return true;
    }
