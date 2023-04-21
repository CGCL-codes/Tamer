    static EnsemblePart FindEnsemblePart(Interp interp, Ensemble ensData, String partName) throws TclException {
        int pos = 0;
        int first, last, nlen;
        int i, cmp;
        EnsemblePart rensPart = null;
        first = 0;
        last = ensData.numParts - 1;
        nlen = partName.length();
        while (last >= first) {
            pos = (first + last) / 2;
            if (partName.charAt(0) == ensData.parts[pos].name.charAt(0)) {
                cmp = partName.substring(0, nlen).compareTo(ensData.parts[pos].name);
                if (cmp == 0) {
                    break;
                }
            } else if (partName.charAt(0) < ensData.parts[pos].name.charAt(0)) {
                cmp = -1;
            } else {
                cmp = 1;
            }
            if (cmp > 0) {
                first = pos + 1;
            } else {
                last = pos - 1;
            }
        }
        if (last < first) {
            return rensPart;
        }
        if (nlen < ensData.parts[pos].minChars) {
            while (pos > 0) {
                pos--;
                if (partName.substring(0, nlen).compareTo(ensData.parts[pos].name) != 0) {
                    pos++;
                    break;
                }
            }
        }
        if (nlen < ensData.parts[pos].minChars) {
            StringBuffer buffer = new StringBuffer(64);
            buffer.append("ambiguous option \"" + partName + "\": should be one of...");
            for (i = pos; i < ensData.numParts; i++) {
                if (partName.substring(0, nlen).compareTo(ensData.parts[i].name) != 0) {
                    break;
                }
                buffer.append("\n  ");
                GetEnsemblePartUsage(ensData.parts[i], buffer);
            }
            throw new TclException(interp, buffer.toString());
        }
        rensPart = ensData.parts[pos];
        return rensPart;
    }
