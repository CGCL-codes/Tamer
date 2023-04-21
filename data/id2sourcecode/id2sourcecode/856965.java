    public void toSpdHead(String strFile) {
        try {
            String strLine = "";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(strFile));
            RandomAccessFile spdRaf = new RandomAccessFile(strFile + ".csv", "rw");
            spdRaf.writeBytes("Spreadsheet Format\nTest Program:\nLot ID:\nOperator:\nComputer:\nDate:\n");
            String strTestNo = ",,";
            String strName = ",,";
            String strNullLine = ",,,";
            String strMax = ",,";
            String strMin = ",,";
            String strUnit = "Serial#,Bin#,";
            while ((strLine = bufferedReader.readLine()) != null) {
                strLine = strLine.trim();
                String[] strLineArr = strLine.split(" +");
                if (strLineArr.length == 11) {
                    strTestNo += strLineArr[0] + ",";
                    strName += strLineArr[1] + ",";
                    strMax += strLineArr[9] + ",";
                    strMin += strLineArr[3] + ",";
                    strUnit += strLineArr[4] + ",";
                }
                if (strLineArr.length == 12) {
                    strTestNo += strLineArr[0] + ",";
                    strName += strLineArr[1] + ",";
                    strMax += strLineArr[10] + ",";
                    strMin += strLineArr[3] + ",";
                    strUnit += strLineArr[4] + ",";
                }
                if (strLine.length() > 6 && strLineArr.length == 2) {
                    if (strLine.substring(0, 3).equals("Bin")) {
                        spdRaf.writeBytes(strTestNo + "\n");
                        spdRaf.writeBytes(strName + "\n");
                        spdRaf.writeBytes(strNullLine + "\n");
                        spdRaf.writeBytes(strMax + "\n");
                        spdRaf.writeBytes(strMin + "\n");
                        spdRaf.writeBytes(strUnit + "\n");
                        return;
                    }
                }
            }
            bufferedReader.close();
            spdRaf.close();
        } catch (Exception e) {
            System.out.println(e + " toSpdHead()");
        }
    }
