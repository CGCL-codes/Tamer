    private static float sqrt(float num, float e) {
        float low = 0F;
        float high = num;
        float guess, e0;
        int count = 0;
        do {
            guess = (low + high) / 2;
            if (guess * guess > num) {
                high = guess;
                e0 = guess * guess - num;
            } else {
                low = guess;
                e0 = num - guess * guess;
            }
            count++;
            System.out.printf("Try %f, e: %f\n", guess, e0);
        } while (e0 > e);
        System.out.printf("Try %d times, result: %f\n", count, guess);
        return guess;
    }
