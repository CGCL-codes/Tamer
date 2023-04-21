    public boolean deleteFileRecord(long indexOfLine) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        try {
            fwriter.seek(0);
            StringBuffer txt = new StringBuffer();
            int c;
            int countLine = 1;
            while (fwriter.read() != -1) {
                fwriter.seek(fwriter.getFilePointer() - 1);
                while (fwriter.read() != '\n') ;
                {
                    fwriter.read();
                    countLine++;
                    long afterLine = fwriter.getFilePointer();
                    if (countLine == indexOfLine) {
                        while (fwriter.read() != '\n') ;
                        while ((c = fwriter.read()) != -1) {
                            txt.append((char) c);
                        }
                        System.out.println(txt.toString());
                        fwriter.seek(afterLine - 1);
                        fwriter.writeBytes(txt.toString());
                        fwriter.setLength(fwriter.getFilePointer());
                    }
                }
            }
            t.end();
            TimerRecordFile timerFile = new TimerRecordFile("combinereport.query", "FileOperation", "deleteFileRecord", t.duration());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("combinereport.query", "FileOperation", "deleteFileRecord", t.duration());
        return false;
    }
