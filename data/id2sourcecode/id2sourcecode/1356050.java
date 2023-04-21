    public void cmdProc(Interp interp, TclObject[] objv) throws TclException {
        int mode = GLOB;
        int dataType = ASCII;
        boolean isIncreasing = true;
        TclObject pattern = null;
        TclObject list = null;
        if (objv.length < 3) {
            throw new TclNumArgsException(interp, 1, objv, "?options? list pattern");
        }
        for (int i = 1; i < objv.length - 2; i++) {
            switch(TclIndex.get(interp, objv[i], options, "option", 0)) {
                case LSEARCH_ASCII:
                    dataType = ASCII;
                    break;
                case LSEARCH_DECREASING:
                    isIncreasing = false;
                    break;
                case LSEARCH_DICTIONARY:
                    dataType = DICTIONARY;
                    break;
                case LSEARCH_EXACT:
                    mode = EXACT;
                    break;
                case LSEARCH_INCREASING:
                    isIncreasing = true;
                    break;
                case LSEARCH_INTEGER:
                    dataType = INTEGER;
                    break;
                case LSEARCH_GLOB:
                    mode = GLOB;
                    break;
                case LSEARCH_REAL:
                    dataType = REAL;
                    break;
                case LSEARCH_REGEXP:
                    mode = REGEXP;
                    break;
                case LSEARCH_SORTED:
                    mode = SORTED;
                    break;
            }
        }
        TclObject[] listv = TclList.getElements(interp, objv[objv.length - 2]);
        TclObject patObj = objv[objv.length - 1];
        String patternBytes = null;
        int patInt = 0;
        double patDouble = 0.0;
        int length = 0;
        if (mode == EXACT || mode == SORTED) {
            switch(dataType) {
                case ASCII:
                case DICTIONARY:
                    patternBytes = patObj.toString();
                    length = patternBytes.length();
                    break;
                case INTEGER:
                    patInt = TclInteger.get(interp, patObj);
                    break;
                case REAL:
                    patDouble = TclDouble.get(interp, patObj);
                    break;
            }
        } else {
            patternBytes = patObj.toString();
            length = patternBytes.length();
        }
        int index = -1;
        if (mode == SORTED) {
            int match = 0;
            int lower = -1;
            int upper = listv.length;
            while (lower + 1 != upper) {
                int i = (lower + upper) / 2;
                switch(dataType) {
                    case ASCII:
                        {
                            String bytes = listv[i].toString();
                            match = patternBytes.compareTo(bytes);
                            break;
                        }
                    case DICTIONARY:
                        {
                            String bytes = listv[i].toString();
                            match = DictionaryCompare(patternBytes, bytes);
                            break;
                        }
                    case INTEGER:
                        {
                            int objInt = TclInteger.get(interp, listv[i]);
                            if (patInt == objInt) {
                                match = 0;
                            } else if (patInt < objInt) {
                                match = -1;
                            } else {
                                match = 1;
                            }
                            break;
                        }
                    case REAL:
                        {
                            double objDouble = TclDouble.get(interp, listv[i]);
                            if (patDouble == objDouble) {
                                match = 0;
                            } else if (patDouble < objDouble) {
                                match = -1;
                            } else {
                                match = 1;
                            }
                            break;
                        }
                }
                if (match == 0) {
                    index = i;
                    upper = i;
                } else if (match > 0) {
                    if (isIncreasing) {
                        lower = i;
                    } else {
                        upper = i;
                    }
                } else {
                    if (isIncreasing) {
                        upper = i;
                    } else {
                        lower = i;
                    }
                }
            }
        } else {
            for (int i = 0; i < listv.length; i++) {
                boolean match = false;
                switch(mode) {
                    case SORTED:
                    case EXACT:
                        {
                            switch(dataType) {
                                case ASCII:
                                    {
                                        String bytes = listv[i].toString();
                                        int elemLen = bytes.length();
                                        if (length == elemLen) {
                                            match = bytes.equals(patternBytes);
                                        }
                                        break;
                                    }
                                case DICTIONARY:
                                    {
                                        String bytes = listv[i].toString();
                                        match = (DictionaryCompare(bytes, patternBytes) == 0);
                                        break;
                                    }
                                case INTEGER:
                                    {
                                        int objInt = TclInteger.get(interp, listv[i]);
                                        match = (objInt == patInt);
                                        break;
                                    }
                                case REAL:
                                    {
                                        double objDouble = TclDouble.get(interp, listv[i]);
                                        match = (objDouble == patDouble);
                                        break;
                                    }
                            }
                            break;
                        }
                    case GLOB:
                        {
                            match = Util.stringMatch(listv[i].toString(), patternBytes);
                            break;
                        }
                    case REGEXP:
                        {
                            match = Util.regExpMatch(interp, listv[i].toString(), patObj);
                            break;
                        }
                }
                if (match) {
                    index = i;
                    break;
                }
            }
        }
        interp.setResult(index);
    }
