    private int solve(int result, int[] m, int pos) {
        if (m.length == pos) {
            if (isPalindrome(m)) {
                return result;
            }
            return Integer.MAX_VALUE;
        }
        if (m[pos] == 0) {
            m[pos] = 1;
            int solve1 = solve(result + oCost, m, pos + 1);
            m[pos] = -1;
            int solve2 = solve(result + xCost, m, pos + 1);
            m[pos] = 0;
            return Math.min(solve1, solve2);
        }
        return solve(result, m, pos + 1);
    }
