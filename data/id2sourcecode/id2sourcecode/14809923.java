    private void populateRandomWalksC(int number, int maxLength) {
        int counter = 0, unsucc = 0;
        FSMStructure fsm = WMethod.getGraphData(g);
        Random length = new Random(0);
        while (counter < number) {
            List<String> path = new ArrayList<String>(maxLength);
            String current = fsm.init;
            if (unsucc > 100) return;
            int randomLength = 0;
            while (randomLength == 0) randomLength = length.nextInt(maxLength + 1);
            for (int i = 0; i < randomLength; i++) {
                Map<String, String> row = fsm.trans.get(current);
                if (row.isEmpty()) break;
                String nextInput = (String) pickRandom(row.keySet());
                path.add(nextInput);
                current = row.get(nextInput);
            }
            int oldSize = sPlus.size();
            sPlus.add(new ArrayList<String>(path));
            if (sPlus.size() > oldSize) {
                counter++;
                unsucc = 0;
            } else {
                unsucc++;
            }
        }
    }
