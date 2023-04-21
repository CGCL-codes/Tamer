    public static String accessPolicyString(int policy) {
        String result = null;
        switch(policy) {
            case 0:
                result = "private";
                break;
            case 1:
                result = "public-read";
                break;
            case 2:
                result = "public-read-write";
                break;
            case 3:
                result = "authenticated-read";
                break;
        }
        return result;
    }
