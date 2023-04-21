    private void getQuestionsFromServer() {
        String text = null;
        String[] questions;
        String[] question;
        try {
            URL url = new URL(GET_QUESTIONS_URL);
            Scanner in = new Scanner(new InputStreamReader(url.openStream())).useDelimiter("\n");
            while (in.hasNext()) {
                text = in.next();
            }
        } catch (Exception e) {
            return;
        }
        questions = text.split(";");
        for (int i = 0; i < questions.length; i++) {
            question = questions[i].split("\\|");
            db.insertQuestion(question[0], question[1], question[2], question[3]);
        }
    }
