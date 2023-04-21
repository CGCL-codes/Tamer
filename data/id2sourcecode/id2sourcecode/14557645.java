    public static int insertUser(Vector users, User user) {
        int insertStart = 0;
        int insertEnd = users.size();
        User middleUser;
        while (insertStart != insertEnd) {
            int middle;
            if (insertStart + 1 != insertEnd) middle = (insertStart + insertEnd) / 2; else middle = insertStart;
            middleUser = (User) users.get(middle);
            if (user.compare(middleUser)) insertEnd = middle; else {
                if (insertStart == middle) insertStart = insertEnd; else insertStart = middle;
            }
        }
        users.insertElementAt(user, insertStart);
        return insertStart;
    }
