    public boolean addRecordToFile(Hashtable<Object, Object> hash) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        System.out.println("hashtable enterd into add record method = " + hash);
        try {
            fwriter.seek(0);
            String line = fwriter.readLine();
            String[] ArrayForSort = line.split(Input.SEPARATOR);
            for (int i = 0; i < ArrayForSort.length; i++) {
                try {
                    ArrayForSort[i] = hash.get(ArrayForSort[i].toLowerCase()).toString();
                } catch (Exception e) {
                    ArrayForSort[i] = "";
                }
                System.out.println(ArrayForSort[i]);
            }
            System.out.println("Appended record is :");
            fwriter.seek(fwriter.length());
            for (int i = 0; i < ArrayForSort.length; i++) {
                System.out.print(ArrayForSort[i] + Input.SEPARATOR);
                fwriter.writeBytes(ArrayForSort[i] + Input.SEPARATOR);
            }
            sortedStringArray = ArrayForSort;
            fwriter.writeBytes(Input.ENDSEPARATOR);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("file", "FileOperation", "addRecordToFile", t.duration());
        return false;
    }
