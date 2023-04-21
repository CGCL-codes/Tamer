    private int calcAge(AlbatrossPerson albatrossPerson) {
        int code = albatrossPerson.AGEP;
        int min = 0;
        int max = 0;
        if (code == 0) {
            min = 18;
            max = 35;
        } else if (code == 1) {
            min = 35;
            max = 55;
        } else if (code == 2) {
            min = 55;
            max = 65;
        } else if (code == 3) {
            min = 65;
            max = 75;
        } else if (code == 4) {
            min = 75;
            max = 100;
        }
        int age = min + random.nextInt(max - min);
        return age;
    }
