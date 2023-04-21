    public SpeechRecognitionResult speechRecognize(String prompt, int timeout) throws AgiException {
        return getChannel().speechRecognize(prompt, timeout);
    }
