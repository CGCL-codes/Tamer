    private String[] xLiner(int number) {
        if (number <= 2) {
            number = 2;
        } else {
            number = 3;
        }
        String[] result = new String[number];
        String[] lastSegs = new String[number - 1];
        String lastWord = "";
        String tempWord = "";
        int[] tempSyls = new int[number];
        int tempSyl = 0;
        boolean match;
        bcWordList lst = null;
        bcWord current = null;
        for (int l = 0; l <= number - 2; l++) {
            do {
                match = true;
                if (l == 0) {
                    currentSyllables = 0;
                    result[0] = fill(parseGrammar(GRAMMAR_START));
                    lastWord = getLastWord(result[0]);
                    tempSyls[0] = currentSyllables;
                    currentSyllables = 0;
                } else {
                    lastWord = getLastWord(result[1]);
                }
                result[l + 1] = parseGrammar(GRAMMAR_START);
                if (result[l + 1].indexOf("@") == -1) {
                    lastSegs[l] = result[l + 1];
                    result[l + 1] = "";
                    tempSyls[l + 1] = currentSyllables = 0;
                } else {
                    for (int j = result[l + 1].length() - 1; j >= 0; j--) {
                        if (result[l + 1].charAt(j) == '@') {
                            lastSegs[l] = result[l + 1].substring(j + 1);
                            result[l + 1] = fill(result[l + 1].substring(0, j));
                            tempSyls[l + 1] = currentSyllables;
                            currentSyllables = 0;
                            break;
                        }
                    }
                }
                if (lastSegs[l].startsWith("'") && lastSegs[l].endsWith("'")) {
                    tempWord = lastSegs[l].substring(1, lastSegs[l].length() - 1);
                    tempSyl = getWordFromLists(tempWord).getSyllables();
                    if ((rhyming(tempWord, lastWord) < 1) || !(tempSyl + tempSyls[l + 1] <= tempSyls[0] + module.getSyllableTolerance()) || !(tempSyl + tempSyls[l + 1] >= tempSyls[0] - module.getSyllableTolerance())) {
                        match = false;
                    } else {
                        result[l + 1] += " " + tempWord;
                        tempSyls[l + 1] += tempSyl;
                    }
                } else {
                    lst = module.getWordList(lastSegs[l]);
                    List possibleMatches = new LinkedList();
                    for (int j = 0; j < lst.getNumberOfWords(); j++) {
                        current = lst.getItem(j);
                        tempWord = current.getWord();
                        if ((rhyming(tempWord, lastWord) == 1) && (current.getSyllables() + tempSyls[l + 1] <= tempSyls[0] + module.getSyllableTolerance()) && (current.getSyllables() + tempSyls[l + 1] >= tempSyls[0] - module.getSyllableTolerance())) {
                            possibleMatches.add(current);
                        }
                    }
                    if (possibleMatches.size() == 0) {
                        match = false;
                    } else {
                        current = (bcWord) possibleMatches.get(r.nextInt(possibleMatches.size()));
                        tempWord = current.getWord();
                        result[l + 1] += " " + tempWord;
                        tempSyls[l + 1] += current.getSyllables();
                    }
                }
                if ((module.getSyllableTolerance() == 0) && (!result[l + 1].equals("")) && (!metricMatch(result[0], result[l + 1]))) {
                    match = false;
                }
            } while (!match);
        }
        return result;
    }
