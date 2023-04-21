    public Vector<Object> getVectorSet(Hashtable<Object, Object> map) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        System.out.println(map);
        Vector<Object> fileResultset = new Vector<Object>();
        try {
            String src = swrite.readLine();
            String[] colHeader = src.split(Input.SEPARATOR);
            while ((src = swrite.readLine()) != null) {
                try {
                    Hashtable<Object, Object> table = new Hashtable<Object, Object>();
                    System.out.println("The Line is : " + src);
                    String[] arr2 = src.split(Input.SEPARATOR);
                    hmIterator = map.keys();
                    while (hmIterator.hasMoreElements()) {
                        try {
                            int colindex = -1;
                            String clmName = (String) hmIterator.nextElement();
                            for (int i = 0; i < colHeader.length; i++) {
                                if (clmName.equalsIgnoreCase(colHeader[i])) colindex = i;
                            }
                            String valOfHash = map.get(clmName).toString();
                            if (arr2[colindex].equals(valOfHash)) {
                                FLAG = true;
                            } else {
                                table.clear();
                                FLAG = false;
                                break;
                            }
                        } catch (Exception e) {
                        }
                    }
                    if (FLAG == true) {
                        for (int i = 0; i < colHeader.length; i++) {
                            try {
                                table.put(colHeader[i], arr2[i]);
                            } catch (Exception e) {
                            }
                        }
                        fileResultset.add(table);
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("file", "SearchRecord", "getVectorSet", t.duration());
        return fileResultset;
    }
