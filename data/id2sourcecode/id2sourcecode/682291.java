    public static void main(String[] args) {
        Dice Di = new Dice();
        Scanner Scan1 = new Scanner(System.in);
        Scanner Scan3 = new Scanner(System.in);
        Random randomNumbers = new Random();
        int sum1 = 0;
        int sum2 = 0;
        int lng;
        int s;
        String player1;
        String player2;
        System.out.println("Please, write the name of the first player: ");
        player1 = Scan1.nextLine();
        System.out.println("Please, write the name of the second player: ");
        player2 = Scan1.nextLine();
        System.out.println("Please, type the \"Length\" of the dice: ");
        lng = Scan3.nextInt();
        System.out.println("It is your turn now " + player1 + " you'll throw 5 times 'pray to get the highest score :P'.\n");
        for (int i = 0; i < 5; i++) {
            s = 0 + randomNumbers.nextInt(100 - 0);
            System.out.println("You got: " + s);
            sum1 = sum1 + s;
        }
        System.out.println("It is your turn now " + player2 + " you'll throw 5 times 'pray to get the highest score :P'.\n");
        for (int j = 0; j < 5; j++) {
            s = 0 + randomNumbers.nextInt(100 - 0);
            System.out.println("You got: " + s);
            sum2 = sum2 + s;
        }
        System.out.println(player1 + ", you got a sum of " + sum1 + " and " + player2 + ", got a sum of " + sum2);
        if (sum1 > sum2) {
            System.out.println(player1 + ", Congratulation! you won :D.");
        } else if (sum2 > sum1) {
            System.out.println(player2 + ", Congratulation! you won :D.");
        } else if (sum2 == sum1) {
            System.out.println(player2 + ", and " + player2 + " you both played a nice game, you two equal in the luck :P.");
        }
    }
