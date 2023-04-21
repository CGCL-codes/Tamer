    protected void doRequest(HttpServletRequest request, HttpServletResponse response, String taskUri, String taskName, String projectUri, String projectName) {
        String queryTask = "SELECT DISTINCT ?task WHERE {" + "?calculation <" + RDF.type.getURI() + "> <" + SF.Calculation.getURI() + "> . " + "?calculation <" + SF.belongsTo.getURI() + "> <" + projectUri + "> . " + "?calculation <" + SF.relatedTask.getURI() + "> ?task . }";
        Iterator iterTask = ResourceModelHolder.executeQuery(organization, queryTask);
        ArrayList taskList = new ArrayList();
        while (iterTask.hasNext()) {
            QuerySolution solution = (QuerySolution) iterTask.next();
            Resource resTask = (Resource) solution.get("task");
            Task task = new Task();
            task.uri = resTask.getURI();
            task.title = resTask.getProperty(DC.title).getLiteral().getString();
            String queryCalc = "SELECT DISTINCT ?calculation WHERE {" + "?calculation <" + RDF.type.getURI() + "> <" + SF.Calculation.getURI() + "> . " + "?calculation <" + SF.belongsTo.getURI() + "> <" + projectUri + "> . " + "?calculation <" + SF.relatedTask.getURI() + "> <" + task.uri + "> . " + "?calculation <" + DC.date.getURI() + "> ?date .} " + "ORDER BY ASC(?date)";
            Iterator iterCalc = ResourceModelHolder.executeQuery(organization, queryCalc);
            while (iterCalc.hasNext()) {
                QuerySolution solution1 = (QuerySolution) iterCalc.next();
                Calculation calculation = new Calculation();
                Resource resCalc = (Resource) solution1.get("calculation");
                calculation.uri = resCalc.getURI();
                calculation.date = resCalc.getProperty(DC.date).getLiteral().getString();
                Statement statement = resCalc.getProperty(RDFS.comment);
                if (statement != null) calculation.comment = statement.getLiteral().getString();
                calculation.id = resCalc.getProperty(DC.identifier).getLiteral().getString();
                statement = resCalc.getProperty(SF.prevResult);
                if (statement != null) calculation.prevCalcUri = statement.getResource().getURI();
                task.calcList.add(calculation);
            }
            task.date = ((Calculation) task.calcList.get(0)).date;
            taskList.add(task);
        }
        Collections.sort(taskList, new TaskComparator());
        response.setContentType("text/xml; charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.println("<?xml version=\"1.0\"?>");
            writer.println("<ShareFastWorkflow Nodes=\"3\" Links=\"2\" Images=\"0\" Sequence=\"3\">");
            writer.println("\t<PageScale>1</PageScale>");
            writer.println("\t<WorkflowUri uri=\"" + TextUtil.xmlEscape(projectUri) + "\" />");
            BufferedReader reader = new BufferedReader(new FileReader(ServerConsoleServlet.convertToAbsolutePath("WEB-INF/conf/default.sfw")));
            while (reader.ready()) writer.println(reader.readLine());
            int y = 50;
            for (Iterator iterator = taskList.iterator(); iterator.hasNext(); ) {
                Task task1 = (Task) iterator.next();
                printComment(writer, 20, y, task1);
                int x = 220;
                for (Iterator iterator1 = task1.calcList.iterator(); iterator1.hasNext(); ) {
                    Calculation calculation1 = (Calculation) iterator1.next();
                    printNode(writer, x, y, calculation1);
                    x += 100;
                }
                y += 100;
            }
            for (int i = 0; i < taskList.size() - 1; i++) {
                Task task1 = (Task) taskList.get(i);
                Task task2 = (Task) taskList.get(i + 1);
                for (Iterator iter2 = task2.calcList.iterator(); iter2.hasNext(); ) {
                    Calculation calc2 = (Calculation) iter2.next();
                    for (Iterator iter1 = task1.calcList.iterator(); iter1.hasNext(); ) {
                        Calculation calc1 = (Calculation) iter1.next();
                        if (calc1.uri.equals(calc2.prevCalcUri)) {
                            printLink(writer, calc1.nodeId, calc2.nodeId);
                            continue;
                        }
                    }
                }
            }
            writer.println("</ShareFastWorkflow>");
            writer.flush();
            writer.close();
            writer = null;
        } catch (IOException e) {
            ServerConsoleServlet.printSystemLog(e.toString() + " " + e.getMessage(), ServerConsoleServlet.LOG_ERROR);
            e.printStackTrace();
        }
    }
