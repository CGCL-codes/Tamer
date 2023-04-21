    private static SortedMap<Integer, Double> calculateAvgTravelTimesPerHour(SortedMap<Integer, SortedMap<Integer, Integer>> data) {
        SortedMap<Integer, Double> hours2TravelTimes = new TreeMap<Integer, Double>();
        for (int i = 6; i < 20; i++) {
            List<Integer> travelTimes = new ArrayList<Integer>();
            Integer lowerBound = i * 3600;
            Integer upperBound = (i + 1) * 3600;
            for (Entry<Integer, SortedMap<Integer, Integer>> entry : data.entrySet()) {
                SortedMap<Integer, Integer> inflowTimes2TravelTimes = entry.getValue();
                for (Entry<Integer, Integer> entroy : inflowTimes2TravelTimes.entrySet()) {
                    Integer inflowTime = entroy.getKey();
                    Integer travelTime = entroy.getValue();
                    if (inflowTime > lowerBound && inflowTime <= upperBound) {
                        travelTimes.add(travelTime);
                    } else {
                    }
                }
            }
            System.out.println(travelTimes);
            Integer sum = 0;
            for (int iterator : travelTimes) {
                sum = sum + iterator;
            }
            Integer mediumHour = (lowerBound + upperBound) / 2;
            Double avgTravelTime = (double) sum / (double) travelTimes.size();
            hours2TravelTimes.put(mediumHour, avgTravelTime);
        }
        return hours2TravelTimes;
    }
