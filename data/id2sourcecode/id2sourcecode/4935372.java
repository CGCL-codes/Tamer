    public void generateInstruments(Arrangement arr) {
        int i = 0;
        for (Iterator iter = lines.iterator(); iter.hasNext(); ) {
            Line line = (Line) iter.next();
            String lineName;
            if (line.isZak()) {
                lineName = "zak" + line.getChannel();
            } else {
                lineName = line.getVarName();
            }
            String lineId = line.getUniqueID();
            String key = "AbstractLineObject." + lineName;
            Object val = arr.getCompilationVariable(key);
            int instrNum = -1;
            Integer lineNum = (Integer) ftableNumMap.get(lineId);
            if (val == null) {
                String instrText = generateLineInstrument(line);
                GenericInstrument instr = new GenericInstrument();
                instr.setText(instrText);
                instrNum = arr.addInstrument(instr);
                arr.setCompilationVariable(key, new Integer(instrNum));
            } else {
                instrNum = ((Integer) val).intValue();
            }
            instrLineArray[i++] = new Integer(instrNum);
            instrLineArray[i++] = lineNum;
        }
    }
