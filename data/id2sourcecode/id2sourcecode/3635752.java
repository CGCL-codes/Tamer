    public int getRecordIndexFile(Hashtable<Object, Object> hash) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        this.hash.putAll(hash);
        try {
            int counter = 1;
            FileUtility fu = new FileUtility(swrite);
            swrite.seek(0);
            String src = swrite.readLine();
            while ((src = swrite.readLine()) != null) {
                counter++;
                System.out.println("The Line is : " + src);
                String[] arr2 = src.split(Input.SEPARATOR);
                hmIterator = hash.keys();
                try {
                    while (hmIterator.hasMoreElements()) {
                        String clmName = (String) hmIterator.nextElement();
                        int colindex = fu.getColumnIndex(clmName);
                        String valOfHash = hash.get(clmName).toString();
                        if (arr2[colindex - 1].equals(valOfHash)) {
                            FLAG = true;
                            break;
                        } else if (Double.parseDouble(arr2[colindex - 1]) == Double.parseDouble((valOfHash))) {
                            FLAG = true;
                            break;
                        }
                    }
                    if (FLAG == true) {
                        System.out.println("The Record Index is : " + counter);
                        return counter;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("file", "SearchRecord", "getRecordIndexFile", t.duration());
        return 0;
    }
