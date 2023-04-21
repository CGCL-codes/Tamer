    public static void main(String[] args) {
        int numberOfCases = Integer.parseInt(readLn());
        for (int k = 0; k < numberOfCases; k++) {
            String line = readLn();
            StringTokenizer stringTokenizer = new StringTokenizer(line);
            int sum = Integer.parseInt(stringTokenizer.nextToken());
            int difference = Integer.parseInt(stringTokenizer.nextToken());
            int a = (sum + difference) / 2;
            int b = a - difference;
            if (a + b == sum && a - b == difference && b >= 0) {
                System.out.println(a + " " + b);
            } else {
                System.out.println("impossible");
            }
        }
    }
