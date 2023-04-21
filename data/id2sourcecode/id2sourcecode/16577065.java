    public ConstantString run(Session session, MaverickString[] args) throws MaverickException {
        ParagraphNode tmp = listener;
        while (tmp != null) {
            tmp.run(session, args);
            tmp = tmp.getNext();
        }
        MaverickString status = session.getStatus();
        session.getChannel(Session.SCREEN_CHANNEL).PRINT(prompt, false, status);
        session.getInputChannel().INPUT(result, ConstantString.ZERO, true, true, status);
        String s = result.toString();
        result.set(prefix);
        result.append(s);
        result.append(suffix);
        return null;
    }
