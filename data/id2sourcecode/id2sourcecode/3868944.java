    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        FifteenEight.blank("Is \"radar\" a palindrome, True or False? " + isPalindrome("radar"));
        FifteenEight.blank();
        FifteenEight.blank("Is \"dogs and cats\" a palindrome, True or False? " + isPalindrome("dogs and cats"));
        FifteenEight.blank();
        FifteenEight.blank("Please enter a word or phrase to check for palindromishness: ");
        String temp = (in.readLine());
        if (isPalindrome(temp) == false) FifteenEight.blank("That is not a palindrome."); else FifteenEight.blank("Yes! That is a palindrome!");
        FifteenEight.blank();
        FifteenEight.blank("Thank you for playing! eop.");
    }
