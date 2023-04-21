    private void appendText(String text) {
        String all = "";
        for (int i = 0; i < chat.length - 1; i++) {
            chat[i] = chat[i + 1];
            all += chat[i] + "\n";
        }
        chat[chat.length - 1] = text;
        all += text + "\n";
        chatArea.setText(all);
    }
