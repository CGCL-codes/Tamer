    private void getPlantAnswersFromServer() {
        String text = null;
        String[] answers;
        String[] answer;
        try {
            URL url = new URL(GET_PLANT_ANSWERS_URL);
            Scanner in = new Scanner(new InputStreamReader(url.openStream())).useDelimiter("\n");
            while (in.hasNext()) {
                text = in.next();
            }
        } catch (Exception e) {
            return;
        }
        answers = text.split(";");
        for (int i = 0; i < answers.length; i++) {
            answer = answers[i].split(",");
            db.insertPlantAnswer(answer[0], answer[1], answer[2], answer[3]);
        }
    }
