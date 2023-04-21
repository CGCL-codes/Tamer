    public int getMaximum(String[] front, int[] back) {
        int N = front.length;
        Item[] items = new Item[N];
        for (int i = 0; i < N; i++) {
            items[i] = new Item(front[i], back[i]);
        }
        Arrays.sort(items);
        int result = 0;
        boolean[] used = new boolean[N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i != j && !used[i] && !used[j] && isPalindrome(items[i].word + items[j].word)) {
                    used[i] = true;
                    used[j] = true;
                    result += items[i].cost + items[j].cost;
                }
            }
        }
        for (int i = 0; i < N; i++) {
            Item item = items[i];
            if (!used[i] && isPalindrome(item.word)) {
                result += items[i].cost;
                break;
            }
        }
        return result;
    }
