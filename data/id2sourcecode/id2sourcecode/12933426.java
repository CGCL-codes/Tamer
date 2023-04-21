    private boolean checkReadWrite(Vector<FSM> fsms) {
        int readerCount = 0;
        int writerCount = 0;
        for (int i = 0; i < fsms.size(); i++) if (fsms.get(i).isReader()) readerCount++; else writerCount++;
        if ((readerCount == 0) || (writerCount == 0)) return false;
        return true;
    }
