    public boolean deleteFileRecord(Hashtable<Object, Object> Condition) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        boolean RESULT = false;
        boolean FLAG = false;
        try {
            int counter = 1;
            FileUtility fu = new FileUtility(fwriter);
            fwriter.seek(0);
            String line = fwriter.readLine();
            StringBuffer data = new StringBuffer();
            data.append(line + Input.ENDSEPARATOR);
            while ((line = fwriter.readLine()) != null) {
                counter++;
                System.out.println("The Line is : " + line);
                String[] arr2 = line.split(Input.SEPARATOR);
                Enumeration<Object> hmIterator = Condition.keys();
                try {
                    while (hmIterator.hasMoreElements()) {
                        try {
                            String clmName = (String) hmIterator.nextElement();
                            int colindex = fu.getColumnIndex(clmName);
                            String valOfHash = Condition.get(clmName).toString();
                            if (arr2[colindex - 1].equals(valOfHash)) {
                                FLAG = true;
                            } else if (Double.parseDouble(arr2[colindex - 1]) == Double.parseDouble((valOfHash))) {
                                FLAG = true;
                            } else {
                                FLAG = false;
                                break;
                            }
                        } catch (Exception e) {
                            FLAG = false;
                        }
                    }
                    if (FLAG == true) {
                        System.out.println("Match Found And Record is deleted");
                        RESULT = true;
                    } else {
                        data.append(line + Input.ENDSEPARATOR);
                    }
                } catch (Exception e) {
                }
            }
            if (RESULT) {
                fwriter.seek(0);
                fwriter.writeBytes(data.toString());
                fwriter.setLength(data.length());
                t.end();
                TimerRecordFile timerFile = new TimerRecordFile("file", "FileOperation", "updateReport", t.duration());
                return RESULT;
            }
        } catch (Exception e) {
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("file", "FileOperation", "deleteFileRecord", t.duration());
        return false;
    }
