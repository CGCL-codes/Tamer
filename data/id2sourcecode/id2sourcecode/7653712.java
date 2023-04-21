    public synchronized boolean updateRecord(Hashtable<Object, Object> hashtableForUppend, Hashtable<Object, Object> Condition) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        boolean FLAG = false;
        try {
            int counter = 1;
            FileUtility fu = new FileUtility(fwriter);
            fwriter.seek(0);
            String line = fwriter.readLine();
            StringBuffer data = new StringBuffer();
            data.append(line + Input.ENDSEPARATOR);
            String[] ArrayForSort = line.split(Input.SEPARATOR);
            for (int i = 0; i < ArrayForSort.length; i++) {
                try {
                    ArrayForSort[i] = hashtableForUppend.get(ArrayForSort[i]).toString();
                } catch (Exception e) {
                    ArrayForSort[i] = "";
                }
            }
            while ((line = fwriter.readLine()) != null) {
                counter++;
                System.out.println("The Line is : " + line);
                String[] arr2 = line.split(Input.SEPARATOR);
                Enumeration<Object> hmIterator = Condition.keys();
                try {
                    FLAG = false;
                    while (hmIterator.hasMoreElements()) {
                        String clmName = (String) hmIterator.nextElement();
                        int colindex = fu.getColumnIndex(clmName);
                        String valOfHash = Condition.get(clmName).toString();
                        try {
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
                        System.out.println("The Record Index is : " + counter);
                        String[] tmpArray = line.split(Input.SEPARATOR);
                        for (int i = 0; i < ArrayForSort.length; i++) {
                            try {
                                if (ArrayForSort[i].length() != 0) {
                                    System.out.print(ArrayForSort[i] + Input.SEPARATOR);
                                    data.append(ArrayForSort[i] + Input.SEPARATOR);
                                } else {
                                    System.out.print(tmpArray[i] + Input.SEPARATOR);
                                    data.append(tmpArray[i] + Input.SEPARATOR);
                                }
                            } catch (Exception e) {
                                data.append("" + Input.SEPARATOR);
                            }
                        }
                        data.append(Input.ENDSEPARATOR);
                    } else data.append(line + Input.ENDSEPARATOR);
                } catch (Exception e) {
                    data.append(line + Input.ENDSEPARATOR);
                    e.printStackTrace();
                }
            }
            fwriter.seek(0);
            System.out.println(Input.SEPARATOR + data.toString());
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
