    private void getAnswersFromServer() {
        String text = null;
        String[] answers;
        String[] answer;
        try {
            URL url = new URL(GET_ANSWERS_URL);
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
            String icon = "";
            String description = "";
            if (answer.length > 3) icon = answer[3];
            if (answer.length > 4) description = answer[4];
            db.insertAnswer(answer[0], answer[1], answer[2], icon, description);
        }
    }
