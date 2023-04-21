    public void getResult() {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        for (int i = starindex; i < buffer.length; i++) {
            for (int j = starindex; j < buffer.length - 1; j++) {
                String splitbufferj[] = buffer[j].split("\t");
                String splitbufferj1[] = buffer[j + 1].split("\t");
                for (int k = 0; k < tempmap.size(); k++) {
                    String leftString = splitbufferj[Integer.parseInt(tempmap.get(k).toString())].trim().toLowerCase();
                    String rightString = splitbufferj1[Integer.parseInt(tempmap.get(k).toString())].toLowerCase().trim();
                    try {
                        double left = Double.parseDouble(leftString);
                        double right = Double.parseDouble(rightString);
                        if (left > right) {
                            String temp = buffer[j];
                            buffer[j] = buffer[j + 1];
                            buffer[j + 1] = temp;
                            break;
                        } else if (left < right) break;
                    } catch (NumberFormatException e) {
                        if (leftString.compareTo(rightString) > 0) {
                            String temp = buffer[j];
                            buffer[j] = buffer[j + 1];
                            buffer[j + 1] = temp;
                            break;
                        } else if (leftString.compareTo(rightString) < 0) break;
                    }
                }
            }
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("sort", "Ascending", "getResult", t.duration());
    }
