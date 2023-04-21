    @Override
    public List<CompletionContext> getCompletions(CompletionContext cc, org.das2.util.monitor.ProgressMonitor mon) throws IOException, UnsupportedAudioFileException {
        List<CompletionContext> result = new ArrayList<CompletionContext>();
        if (cc.context.equals(CompletionContext.CONTEXT_PARAMETER_NAME)) {
            result.add(new CompletionContext(CompletionContext.CONTEXT_PARAMETER_NAME, "offset=", "offset in seconds"));
            result.add(new CompletionContext(CompletionContext.CONTEXT_PARAMETER_NAME, "length=", "length in seconds"));
            result.add(new CompletionContext(CompletionContext.CONTEXT_PARAMETER_NAME, "channel=", "channel number"));
        } else if (cc.context.equals(CompletionContext.CONTEXT_PARAMETER_VALUE)) {
            String paramName = CompletionContext.get(CompletionContext.CONTEXT_PARAMETER_NAME, cc);
            if (paramName.equals("channel")) {
                int channels = getChannels(cc.resourceURI, mon);
                for (int i = 0; i < channels; i++) {
                    result.add(new CompletionContext(CompletionContext.CONTEXT_PARAMETER_VALUE, "" + i));
                }
            } else {
                result.add(new CompletionContext(CompletionContext.CONTEXT_PARAMETER_VALUE, "<double>"));
            }
        }
        return result;
    }
