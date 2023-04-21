    private static String getLikeDescription(int likeLow, int likeHigh) {
        int passnum = likeLow;
        if (likeHigh != -1) {
            passnum = likeLow + random.nextInt(likeHigh - likeLow);
        }
        return getPassenger(passnum).getDescription();
    }
