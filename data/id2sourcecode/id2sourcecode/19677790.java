    private void generate() {
        Document master = loadXML(masterFilePath);
        if (null == master) {
            error("Can not load master document file");
            return;
        }
        Set<String> existingIdentifiers = getIdentifiers(master);
        CheckboxList list;
        JTextField textField;
        JTextArea textArea;
        ComponentInfo componentInfo;
        int selectedIndex;
        Element element;
        String description, notes, taskName;
        list = (CheckboxList) namedComponents.get("list-templates");
        selectedIndex = list.getSelectedIndex();
        if (selectedIndex < 0) {
            request("Please select an activity template", list);
            return;
        }
        componentInfo = activityDocumentTemplates.get(selectedIndex);
        if (null == projectInfo) {
            request("Please select a presentation", namedComponents.get("list-presentations"));
            return;
        }
        debug("project: " + projectInfo.componentFullPath);
        textArea = (JTextArea) namedComponents.get("description");
        description = textArea.getText();
        textArea = (JTextArea) namedComponents.get("notes");
        notes = textArea.getText();
        textField = (JTextField) namedComponents.get("task-name");
        taskName = filter(textField.getText(), patternXmlName);
        if (taskName.equals(textField.getText())) {
            if (0 == taskName.length()) {
                request("Please write a task name.", textField);
                return;
            }
        } else {
            textField.setText(taskName);
            if (0 == taskName.length()) {
                request("Please write a valid task name.<br/>Invalid characters like spaces,<br/>slashes, etc. will be removed.", textField);
                return;
            }
        }
        Document template, initialModel;
        List<Document> constructionModels, misconstructionModels;
        constructionModels = new ArrayList<Document>();
        misconstructionModels = new ArrayList<Document>();
        Iterator<Document> documentIterator;
        template = loadXML(componentInfo.componentFullPath);
        element = find("/ActivityDocument", template, "Activity document root element not found");
        if (null == element) return;
        element.setAttribute("name", taskName);
        element = find("/ActivityDocument/ActivityDocumentComponentList/ActivityDocumentComponent[@title='TrainTrack Model']", template, "Can't find ActivityDocumentComponent with title 'TrainTrack Model' in template");
        if (null == element) return;
        insertModel(loadXML(new File(projectInfo.componentFullPath)), element, existingIdentifiers);
        list = (CheckboxList) namedComponents.get("list-initial-models");
        initialModel = loadXML(projectInfo.componentFullPath);
        element = find("/activity-description/rulesReasoner", master, "Can't find rulesReasoner in the master file");
        if (null == element) return;
        list = (CheckboxList) namedComponents.get("list-reasoners");
        selectedIndex = list.getSelectedIndex();
        if (selectedIndex >= 0) {
            ReasonerType reasoner = reasoners.get(selectedIndex);
            element.setAttribute("id", Integer.toString(reasoner.getId()));
            Node comment = master.createComment(" " + list.getSelectedButton().getText() + " ");
            Node next = element.getNextSibling();
            if (next != null) {
                element.getParentNode().insertBefore(comment, next);
            } else {
                element.getParentNode().appendChild(comment);
            }
        }
        list = (CheckboxList) namedComponents.get("list-constructions");
        for (int i = 0; i < list.getComponentCount(); ++i) {
            JToggleButton item = (JToggleButton) list.getComponent(i);
            if (item.isSelected()) {
                String modelFilePath = constructions.get(i).componentFullPath;
                constructionModels.add(loadXML(modelFilePath));
            }
        }
        list = (CheckboxList) namedComponents.get("list-misconstructions");
        for (int i = 0; i < list.getComponentCount(); ++i) {
            JToggleButton item = (JToggleButton) list.getComponent(i);
            if (item.isSelected()) {
                String modelFilePath = misconstructions.get(i).componentFullPath;
                misconstructionModels.add(loadXML(modelFilePath));
            }
        }
        element = find("/activity-description/goalsDefinition", master, "Can't find goalsDefinition in the master file");
        if (null == element) return;
        while (element.hasChildNodes()) element.removeChild(element.getFirstChild());
        list = (CheckboxList) namedComponents.get("list-goals");
        for (int i = 0; i < list.getComponentCount(); ++i) {
            JToggleButton item = (JToggleButton) list.getComponent(i);
            Goal goal = goals.get(i);
            if (item.isSelected()) {
                Element goalElement = master.createElement("goal");
                goalElement.setAttribute("id", Integer.toString(goal.getId()));
                element.appendChild(master.createTextNode("\n        "));
                element.appendChild(goalElement);
                element.appendChild(master.createComment(" " + item.getText() + " "));
            }
        }
        element.appendChild(master.createTextNode("\n    "));
        element = find("/activity-description/learningObjectives", master, "Can't find learningObjectives in the master file");
        if (null == element) return;
        while (element.hasChildNodes()) element.removeChild(element.getFirstChild());
        list = (CheckboxList) namedComponents.get("list-learning-objectives");
        for (int i = 0; i < list.getComponentCount(); ++i) {
            JToggleButton item = (JToggleButton) list.getComponent(i);
            AbstractLearnerModelAttribute objective = learningObjectives.get(i);
            if (item.isSelected()) {
                Element objectiveElement = master.createElement("learningObjective");
                objectiveElement.setAttribute("id", Integer.toString(objective.getId()));
                element.appendChild(master.createTextNode("\n        "));
                element.appendChild(objectiveElement);
                element.appendChild(master.createComment(" " + item.getText() + " "));
            }
        }
        element.appendChild(master.createTextNode("\n    "));
        element = find("/activity-description/student-description", master, "Can't find student-description in the master file");
        if (null == element) return;
        while (element.hasChildNodes()) element.removeChild(element.getFirstChild());
        element.appendChild(master.createTextNode("\n" + description + "\n"));
        element = find("/activity-description/notes", master, "Can't find notes in the master file");
        if (null == element) return;
        while (element.hasChildNodes()) element.removeChild(element.getFirstChild());
        element.appendChild(master.createTextNode("\n" + notes + "\n"));
        element = find("/activity-description/activity-document", master, "Can't find activity-document in the master file");
        if (null == element) return;
        insertModel(template, element, existingIdentifiers);
        element = find("/activity-description/initialModel", master, "Can't find initialModel in the master file");
        if (null == element) return;
        insertModel(initialModel, element, existingIdentifiers);
        element = find("/activity-description/expected-constructions", master, "Can't find expected-constructions in the master file");
        if (null == element) return;
        documentIterator = constructionModels.iterator();
        while (documentIterator.hasNext()) {
            insertModel(documentIterator.next(), element, existingIdentifiers);
        }
        element = find("/activity-description/common-misconstructions", master, "Can't find common-misconstructions in the master file");
        if (null == element) return;
        documentIterator = misconstructionModels.iterator();
        while (documentIterator.hasNext()) {
            insertModel(documentIterator.next(), element, existingIdentifiers);
        }
        getIdentifiers(master);
        String fileName = filter(taskName + ".xml", patternFileName);
        File file = new File(getDirectory(), fileName);
        if (!file.exists() || overwrite || (overwrite = confirm("Warning", "There is already a file called\n" + fileName + "\n\nOverwrite it?"))) {
            saveXML(master, file);
        }
    }
