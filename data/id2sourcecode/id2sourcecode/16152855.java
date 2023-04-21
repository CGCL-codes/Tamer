    public static void main(String[] args) {
        final List<Integer> temp = new ArrayList<Integer>();
        for (int i = 999; i > 1; i--) for (int j = 999; j > 1; j--) {
            final int k = i * j;
            if (isPalindrome(String.valueOf(k))) temp.add(k);
        }
        Collections.sort(temp);
        System.out.println(temp.get(temp.size() - 1));
    }
