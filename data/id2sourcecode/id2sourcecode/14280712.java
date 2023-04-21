    public SpeechRecognitionResult speechRecognize(String prompt, int timeout, int offset) throws AgiException {
        return getChannel().speechRecognize(prompt, timeout, offset);
    }
