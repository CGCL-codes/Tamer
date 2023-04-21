    private void getPlantsFromServer() {
        String text = null;
        String[] answers;
        String[] answer;
        try {
            URL url = new URL(GET_PLANTS_URL);
            Scanner in = new Scanner(new InputStreamReader(url.openStream())).useDelimiter("\n");
            while (in.hasNext()) {
                text = in.next();
            }
        } catch (Exception e) {
            return;
        }
        answers = text.split(";");
        for (int i = 0; i < answers.length; i++) {
            answer = answers[i].split("\\|");
            String category = "";
            String imageURL = "";
            String imageAttribution = "";
            String scientificName = "";
            if (answer.length > 2) category = answer[2];
            if (answer.length > 3) scientificName = answer[3];
            if (answer.length > 4) imageURL = answer[4];
            if (answer.length > 5) imageAttribution = answer[5];
            db.insertPlant(answer[0], answer[1], category, scientificName, imageURL, imageAttribution);
        }
    }
