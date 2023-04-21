    protected void resolveMessage() {
        String[] lines = message.split(LINE_SEPARATOR);
        header = lines[0];
        if (lines.length > 1) {
            bodyEntities = new String[lines.length - 1];
            for (int i = 0; i < lines.length - 1; i++) {
                bodyEntities[i] = lines[i + 1];
            }
        }
    }
