    public static XTrace createTrace(int minLength, int maxLength) {
        XTrace trace = factory.createTrace();
        addAttributes(trace, 0.9, 3, 50);
        int length = minLength + random.nextInt(maxLength - minLength);
        for (int i = 0; i < length; i++) {
            trace.add(createEvent());
        }
        NUM_TRACES++;
        return trace;
    }
