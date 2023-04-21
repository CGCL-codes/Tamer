    public static String generate(int minLength, int maxLength) throws Exception {
        PasswordGenerator.init();
        if (minLength <= 0 || maxLength <= 0 || minLength > maxLength) return null;
        char[][] charGroups = new char[][] { PASSWORD_CHARS_LCASE.toCharArray(), PASSWORD_CHARS_UCASE.toCharArray(), PASSWORD_CHARS_NUMERIC.toCharArray(), PASSWORD_CHARS_SPECIAL.toCharArray() };
        int[] charsLeftInGroup = new int[charGroups.length];
        for (int i = 0; i < charsLeftInGroup.length; i++) charsLeftInGroup[i] = charGroups[i].length;
        int[] leftGroupsOrder = new int[charGroups.length];
        for (int i = 0; i < leftGroupsOrder.length; i++) leftGroupsOrder[i] = i;
        char[] password = null;
        if (minLength < maxLength) password = new char[random.nextInt(maxLength - minLength) + minLength]; else password = new char[minLength];
        int nextCharIdx;
        int nextGroupIdx;
        int nextLeftGroupsOrderIdx;
        int lastCharIdx;
        int lastLeftGroupsOrderIdx = leftGroupsOrder.length - 1;
        for (int i = 0; i < password.length; i++) {
            if (lastLeftGroupsOrderIdx == 0) nextLeftGroupsOrderIdx = 0; else nextLeftGroupsOrderIdx = random.nextInt(lastLeftGroupsOrderIdx);
            nextGroupIdx = leftGroupsOrder[nextLeftGroupsOrderIdx];
            lastCharIdx = charsLeftInGroup[nextGroupIdx] - 1;
            if (lastCharIdx == 0) nextCharIdx = 0; else nextCharIdx = random.nextInt(lastCharIdx + 1);
            password[i] = charGroups[nextGroupIdx][nextCharIdx];
            if (lastCharIdx == 0) charsLeftInGroup[nextGroupIdx] = charGroups[nextGroupIdx].length; else {
                if (lastCharIdx != nextCharIdx) {
                    char temp = charGroups[nextGroupIdx][lastCharIdx];
                    charGroups[nextGroupIdx][lastCharIdx] = charGroups[nextGroupIdx][nextCharIdx];
                    charGroups[nextGroupIdx][nextCharIdx] = temp;
                }
                charsLeftInGroup[nextGroupIdx]--;
            }
            if (lastLeftGroupsOrderIdx == 0) lastLeftGroupsOrderIdx = leftGroupsOrder.length - 1; else {
                if (lastLeftGroupsOrderIdx != nextLeftGroupsOrderIdx) {
                    int temp = leftGroupsOrder[lastLeftGroupsOrderIdx];
                    leftGroupsOrder[lastLeftGroupsOrderIdx] = leftGroupsOrder[nextLeftGroupsOrderIdx];
                    leftGroupsOrder[nextLeftGroupsOrderIdx] = temp;
                }
                lastLeftGroupsOrderIdx--;
            }
        }
        return new String(password);
    }
