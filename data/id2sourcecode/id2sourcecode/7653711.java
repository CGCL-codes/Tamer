    public synchronized boolean updateRecord(long indexOfLine, Hashtable<Object, Object> hashtableForUppend) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        try {
            fwriter.seek(0);
            int s = hashtableForUppend.size();
            String ArrayForSort[] = new String[s];
            int i = 0;
            String line = fwriter.readLine();
            ArrayForSort = line.split(Input.SEPARATOR);
            for (i = 0; i < ArrayForSort.length; i++) {
                try {
                    ArrayForSort[i] = hashtableForUppend.get(ArrayForSort[i].toLowerCase()).toString();
                } catch (Exception e) {
                    ArrayForSort[i] = "";
                }
                System.out.println(ArrayForSort[i]);
            }
            System.out.println("Update record is :");
            int countLine = 1;
            StringBuffer data = new StringBuffer();
            data.append(line + Input.ENDSEPARATOR);
            while ((line = fwriter.readLine()) != null) {
                countLine++;
                data.append(line + "\n");
                if (countLine == (indexOfLine - 1)) {
                    line = fwriter.readLine();
                    String[] tmpArray = line.split(Input.SEPARATOR);
                    for (i = 0; i < ArrayForSort.length; i++) {
                        try {
                            if (ArrayForSort[i].length() != 0) {
                                System.out.print(ArrayForSort[i] + "\t");
                                data.append(ArrayForSort[i] + "\t");
                            } else {
                                System.out.print(tmpArray[i] + "\t");
                                data.append(tmpArray[i] + "\t");
                            }
                        } catch (Exception e) {
                            data.append("" + "\t");
                        }
                    }
                    data.append("\n");
                    countLine++;
                }
            }
            fwriter.seek(0);
            System.out.println("\n" + data.toString());
            fwriter.writeBytes(data.toString());
            fwriter.setLength(data.length());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("file", "FileOperation", "updateRecord", t.duration());
        return false;
    }
