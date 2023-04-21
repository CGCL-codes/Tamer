    @Override
    public void visitNewRead(String readId, int offset, Range validRange, SequenceDirection dir) {
        if (readId.equals("SBPQA03T48E02PA1950R")) {
            System.out.println("here");
        }
        writer.printf("\tRead %s start = %d validRange = %s dir = %s%n", readId, offset, validRange, dir);
    }
