    private FramedMessage createFramedMessage(int headerLength) throws IOException {
        int MAX = (int) Math.pow(2, 8 * headerLength) - 1;
        System.out.println("MAX = " + MAX);
        final int MESSAGE_LENGTH = random.nextInt(MAX + 1);
        System.out.println("MESSAGE_LENGTH = " + MESSAGE_LENGTH + " Byte");
        return createFramedMessage(headerLength, MESSAGE_LENGTH);
    }
