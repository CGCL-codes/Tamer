    public Hashtable getRecordsetHash(Hashtable<Object, Object> hash) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        Hashtable<Object, Object> temp = new Hashtable<Object, Object>();
        System.out.println(hash);
        try {
            FileUtility fu = new FileUtility(swrite);
            swrite.seek(0);
            swrite.readLine();
            while (swrite.read() != -1) {
                swrite.seek(swrite.getFilePointer() - 1);
                String src = swrite.readLine();
                System.out.println("The Line is : " + src);
                String[] arr2 = src.split(Input.SEPARATOR);
                hmIterator = hash.keys();
                while (hmIterator.hasMoreElements()) {
                    String clmName = (String) hmIterator.nextElement();
                    int colindex = fu.getColumnIndex(clmName);
                    String valOfHash = hash.get(clmName).toString();
                    if (arr2[colindex - 1].equals(valOfHash)) {
                        FLAG = true;
                    } else {
                        FLAG = false;
                        break;
                    }
                }
                if (FLAG == true) {
                    System.out.println(arr2.length);
                    for (int i = 0; i < arr2.length; i++) {
                        temp.put(fu.getColumnName(i + 1), arr2[i]);
                    }
                    return temp;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("file", "SearchRecord", "getRecordsetHash", t.duration());
        return null;
    }
