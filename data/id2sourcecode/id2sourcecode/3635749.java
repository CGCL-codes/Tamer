    public String[] getRecordArray(Hashtable<Object, Object> map) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        System.out.println(map);
        Vector<Object> fileResultset = new Vector<Object>();
        try {
            FileUtility fu = new FileUtility(swrite);
            swrite.seek(0);
            swrite.readLine();
            while (swrite.read() != -1) {
                try {
                    swrite.seek(swrite.getFilePointer() - 1);
                    String src = swrite.readLine();
                    System.out.println("The Line is : " + src);
                    String[] arr2 = src.split(Input.SEPARATOR);
                    hmIterator = map.keys();
                    while (hmIterator.hasMoreElements()) {
                        try {
                            String clmName = (String) hmIterator.nextElement();
                            int colindex = fu.getColumnIndex(clmName);
                            String valOfHash = map.get(clmName).toString();
                            if (arr2[colindex - 1].equals(valOfHash)) {
                                FLAG = true;
                            } else {
                                FLAG = false;
                                break;
                            }
                        } catch (Exception e) {
                        }
                    }
                    if (FLAG == true) fileResultset.add(src);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
        String finalRecordset[] = new String[fileResultset.size()];
        for (int i = 0; i < finalRecordset.length; i++) {
            finalRecordset[i] = fileResultset.get(i).toString();
            System.out.println("The Return record is : " + finalRecordset[i].toString());
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("file", "SearchRecord", "getRecordArray", t.duration());
        return finalRecordset;
    }
