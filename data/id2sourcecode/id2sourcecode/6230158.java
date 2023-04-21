    boolean toggleSelection(TreeItem item, boolean isCtrlKeyHold, boolean isShiftKeyHold) {
        if (item == null) {
            return false;
        }
        if ((style & SWT.MULTI) != 0 && (isCtrlKeyHold || isShiftKeyHold)) {
            if (isCtrlKeyHold) {
                for (int i = 0; i < selections.length; i++) {
                    if (item == selections[i]) {
                        TreeItem[] newSelections = new TreeItem[selections.length];
                        for (int j = 0; j < i; j++) {
                            newSelections[j] = selections[j];
                        }
                        for (int j = i; j < selections.length - 1; j++) {
                            newSelections[j] = selections[j + 1];
                        }
                        selections = newSelections;
                        item.showSelection(false);
                        lastSelection = item;
                        return false;
                    }
                }
                selections[selections.length] = item;
                lastSelection = item;
                item.showSelection(true);
            } else {
                for (int i = 0; i < selections.length; i++) {
                    if (selections[i] != null) {
                        selections[i].showSelection(false);
                    }
                }
                if (lastSelection != null) {
                    int idx1 = Math.min(lastSelection.index, item.index);
                    int idx2 = Math.max(lastSelection.index, item.index);
                    selections = new TreeItem[0];
                    for (int i = idx1; i <= idx2; i++) {
                        TreeItem ti = items[i];
                        if (ti.handle.style.display != "none") {
                            selections[selections.length] = ti;
                            ti.showSelection(true);
                        }
                    }
                    return true;
                } else {
                    if (selections.length != 1) {
                        selections = new TreeItem[1];
                    }
                    selections[0] = item;
                }
            }
        } else {
            item.showSelection(true);
            for (int i = 0; i < selections.length; i++) {
                if (selections[i] != null && selections[i] != item) {
                    selections[i].showSelection(false);
                }
            }
            if (selections.length != 1) {
                selections = new TreeItem[1];
            }
            selections[0] = item;
        }
        lastSelection = item;
        return true;
    }
