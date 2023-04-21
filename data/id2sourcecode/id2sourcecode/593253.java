    public static void main(String args[]) {
        int numberCommandStrings = 10000;
        GnubertProperties propertyList = new GnubertProperties();
        GnubertStatus currentStatus = new GnubertStatus();
        String commandStrings[] = new String[numberCommandStrings];
        double fitnessArray[] = new double[numberCommandStrings];
        int counter, index, bucket, tournamentSize, chance;
        int tournamentIndexes[] = null;
        boolean accepted = false;
        Random rand = new Random();
        double temp;
        int sizeImmigrantQueue = 10;
        LinkedList immigrantQueue = new LinkedList();
        initializeStrings(commandStrings, numberCommandStrings);
        int countX, countY;
        FileInputStream filein = null;
        BufferedReader reader = null;
        try {
            filein = new FileInputStream("maze");
        } catch (Exception e) {
            System.out.println("Something is wrong with your maze file!!!");
            System.out.println(e);
            System.exit(1);
        }
        reader = new BufferedReader(new InputStreamReader(filein));
        try {
            try {
                xSize = Integer.valueOf(reader.readLine()).intValue();
                ySize = Integer.valueOf(reader.readLine()).intValue();
                maze = new int[xSize][ySize];
                for (countY = 0; countY < ySize; countY++) for (countX = 0; countX < xSize; countX++) maze[countX][countY] = Integer.valueOf(reader.readLine()).intValue();
            } catch (NumberFormatException e) {
                System.out.println("Something is wrong with your maze file!!!");
                System.out.println(e);
                System.exit(1);
            }
        } catch (IOException e) {
            System.out.println("Your maze file is too short or the dimensions are wrong!");
            System.out.println(e);
            System.exit(1);
        }
        worstCase = Math.sqrt(Math.pow(xSize - 1, 2) + Math.pow(ySize - 1, 2));
        for (countX = 0; countX < xSize + 2; countX++) System.out.print("#");
        System.out.println();
        for (countY = 0; countY < ySize; countY++) {
            System.out.print("|");
            for (countX = 0; countX < xSize; countX++) if (maze[countX][countY] == 1) {
                System.out.print("#");
            } else System.out.print(" ");
            System.out.println("|");
        }
        for (countX = 0; countX < xSize + 2; countX++) System.out.print("#");
        System.out.println();
        HostCatcher hostCatcher = new HostCatcher(propertyList, currentStatus);
        hostCatcher.start();
        System.out.println("HostCatcher started!");
        clientThread = new GnubertClient(hostCatcher, propertyList, currentStatus);
        clientThread.start();
        System.out.println("Client Started!");
        GnubertServer serverThread = new GnubertServer(commandStrings, numberCommandStrings, propertyList, hostCatcher, currentStatus);
        serverThread.start();
        System.out.println("Server Started!");
        for (counter = 0; counter < numberCommandStrings; counter++) {
            fitnessArray[counter] = fitness(commandStrings[counter]);
            if (fitnessArray[counter] == 0) isSolutionInside = true;
        }
        if (isSolutionInside) {
            System.out.println("Solution Inside");
            for (counter = 0; counter < numberCommandStrings; counter++) System.out.println(fitnessArray[counter]);
        } else System.out.println("Fitness level is equal to the percentage of the distance from the starting point (upper Left) to the ending point (Lower Right) that the string reaches.");
        int generationCounter = 0;
        while (!isSolutionInside) {
            tournamentSize = (int) (rand.nextDouble() * (numberCommandStrings - 2)) + 2;
            tournamentIndexes = new int[tournamentSize];
            for (counter = 0; counter < tournamentSize; counter++) {
                while (!accepted) {
                    accepted = true;
                    tournamentIndexes[counter] = (int) (rand.nextDouble() * numberCommandStrings);
                    for (index = 0; index < counter; index++) {
                        if (tournamentIndexes[counter] == tournamentIndexes[index]) accepted = false;
                        break;
                    }
                }
                accepted = false;
            }
            accepted = false;
            index = tournamentSize;
            while (!accepted) {
                accepted = true;
                index--;
                for (counter = 0; counter < index; counter++) {
                    if (fitnessArray[tournamentIndexes[counter]] > fitnessArray[tournamentIndexes[counter + 1]]) {
                        bucket = tournamentIndexes[counter];
                        tournamentIndexes[counter] = tournamentIndexes[counter + 1];
                        tournamentIndexes[counter + 1] = bucket;
                        accepted = false;
                    }
                }
            }
            for (counter = tournamentSize / 2; counter < tournamentSize; counter++) {
                chance = (int) (rand.nextDouble() * 100);
                if (chance < 10) {
                    index = (int) (rand.nextDouble() * tournamentSize / 2);
                    commandStrings[tournamentIndexes[counter]] = duplicateSubstring(commandStrings[tournamentIndexes[index]]);
                    fitnessArray[tournamentIndexes[counter]] = fitness(commandStrings[tournamentIndexes[counter]]);
                    if (fitnessArray[tournamentIndexes[counter]] == 0) isSolutionInside = true;
                } else if (chance < 20) {
                    index = (int) (rand.nextDouble() * tournamentSize / 2);
                    commandStrings[tournamentIndexes[counter]] = deleteSubstring(commandStrings[tournamentIndexes[index]]);
                    fitnessArray[tournamentIndexes[counter]] = fitness(commandStrings[tournamentIndexes[counter]]);
                    if (fitnessArray[tournamentIndexes[counter]] == 0) isSolutionInside = true;
                } else if (chance < 70) {
                    index = (int) (rand.nextDouble() * tournamentSize / 2);
                    commandStrings[tournamentIndexes[counter]] = mutate(commandStrings[tournamentIndexes[index]]);
                    fitnessArray[tournamentIndexes[counter]] = fitness(commandStrings[tournamentIndexes[counter]]);
                    if (fitnessArray[tournamentIndexes[counter]] == 0) isSolutionInside = true;
                } else if (chance < 90) {
                    index = (int) (rand.nextDouble() * tournamentSize / 2);
                    commandStrings[tournamentIndexes[counter]] = recombination(commandStrings[tournamentIndexes[counter]], commandStrings[tournamentIndexes[index]]);
                    fitnessArray[tournamentIndexes[counter]] = fitness(commandStrings[tournamentIndexes[counter]]);
                    if (fitnessArray[tournamentIndexes[counter]] == 0) isSolutionInside = true;
                } else if (chance < 95) {
                    String immigrant = null;
                    immigrant = clientThread.getImmigrant();
                    if (immigrant != null) {
                        commandStrings[tournamentIndexes[counter]] = immigrant;
                        fitnessArray[tournamentIndexes[counter]] = fitness(commandStrings[tournamentIndexes[counter]]);
                        if (fitnessArray[tournamentIndexes[counter]] == 0) isSolutionInside = true;
                    }
                } else if (chance < 100) {
                    index = (int) (rand.nextDouble() * tournamentSize / 2);
                    commandStrings[tournamentIndexes[counter]] = commandStrings[tournamentIndexes[index]];
                    fitnessArray[tournamentIndexes[counter]] = fitness(commandStrings[tournamentIndexes[counter]]);
                }
            }
            if (isSolutionInside) {
                System.out.println("\nOne or more solutions found in generation number " + generationCounter + "!");
                for (counter = 0; counter < numberCommandStrings; counter++) {
                    if (fitnessArray[counter] == 0) {
                        System.out.println("Solution:");
                        System.out.println(commandStrings[counter]);
                    }
                }
            } else {
                temp = 0;
                for (counter = 0; counter < numberCommandStrings; counter++) temp += fitnessArray[counter];
                System.out.print("\rAverage fitness level from generation " + generationCounter + " was " + (1 - temp / numberCommandStrings) * 100 + "% ");
                generationCounter++;
            }
        }
    }
