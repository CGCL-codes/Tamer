    private ArrayList addObject(Object newData, int addNum) {
        ArrayList newObjects = new ArrayList();
        for (int i = 0; i < data.size(); i++) {
            if (sameObject(data.get(i), newData)) {
                String name = nameObject(newData);
                Object[] options = { "Overwrite", "Ignore", "Rename" };
                int n = JOptionPane.showOptionDialog(JOptionPane.getFrameForComponent(component), "A " + typeObject() + " named \"" + name + "\" already exists." + StringUtil.newline + "Would you like to Overwrite the existing " + typeObject() + StringUtil.newline + "Ignore the new version, or Rename the new version?", "Overwrite, Ignore, or Rename " + typeObject() + "?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == JOptionPane.CANCEL_OPTION) {
                    Object update = editObject(newData);
                    if (update == null) return addObject(newData, addNum);
                    return addObject(update, addNum);
                } else if (n == JOptionPane.NO_OPTION) {
                    return data;
                }
            } else {
                newObjects.add(data.get(i));
            }
        }
        if ((addNum < 0) || (addNum > newObjects.size())) newObjects.add(newData); else newObjects.add(addNum, newData);
        return newObjects;
    }
