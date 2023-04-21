        private void handleAlreadyExists(Dictionary dic) {
            if (overwriteAll) {
                addNewWord(dic);
            } else if (skipAll) {
                return;
            } else {
                Integer answer = lOptions.showOptionPane(importWindow.getDialog(), "Dictionary: " + dic.printable() + " \nalready exist in lesson " + selectedLesson.getName() + ". Do you want to overwrite, skip or interrupt import?", "Word already exists", "Overwrite", "Overwrite All", "Skip", "Skip All", "Cancel Import");
                switch(answer) {
                    case 0:
                        addNewWord(dic);
                        break;
                    case 1:
                        overwriteAll = true;
                        addNewWord(dic);
                        break;
                    case 2:
                        return;
                    case 3:
                        skipAll = true;
                        return;
                    case 4:
                        interrupted = true;
                        return;
                    default:
                        break;
                }
            }
        }
