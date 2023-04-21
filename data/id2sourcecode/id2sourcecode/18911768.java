    private static void saveScheduleDialog() {
        String fileName = schedule.getPeriod();
        if (fileName.isEmpty()) {
            System.out.println("Please (re)name the schedule first. (name cannot be empty)");
            return;
        }
        fileName += ".dat";
        File saveFile = new File(fileName);
        if (saveFile.exists()) {
            String answer;
            boolean ok = false;
            do {
                System.out.println("\"" + fileName + "\" already exists. Overwrite? (y or n)");
                answer = input.nextLine().trim();
                if (answer.equals("y")) ok = true; else {
                    System.out.println("Not saved.");
                    return;
                }
            } while (!ok);
        }
        save(fileName);
        System.out.println("Schedule saved as \"" + fileName + "\"");
    }
