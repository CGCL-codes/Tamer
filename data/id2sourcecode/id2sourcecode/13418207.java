    public static void generateScoreEntry(final JspWriter writer, final ServletContext application) throws IOException {
        final Document document = ApplicationAttributes.getChallengeDocument(application);
        final Formatter formatter = new Formatter(writer);
        final Element rootElement = document.getDocumentElement();
        final Element performanceElement = (Element) rootElement.getElementsByTagName("Performance").item(0);
        for (final Element goalEle : new NodelistElementCollectionAdapter(performanceElement.getChildNodes())) {
            final String goalEleName = goalEle.getNodeName();
            if ("computedGoal".equals(goalEleName) || "goal".equals(goalEleName)) {
                final String name = goalEle.getAttribute("name");
                final String title = goalEle.getAttribute("title");
                try {
                    writer.println("<!-- " + name + " -->");
                    writer.println("<tr>");
                    writer.println("  <td>");
                    writer.println("    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size='3'><b>" + title + ":</b></font>");
                    writer.println("  </td>");
                    if ("computedGoal".equals(goalEleName)) {
                        writer.println("  <td colspan='2' align='center'><b>Computed Goal</b></td>");
                    } else if ("goal".equals(goalEleName)) {
                        if (XMLUtils.isEnumeratedGoal(goalEle)) {
                            writer.println("  <td>");
                            generateEnumeratedGoalButtons(goalEle, name, writer);
                            writer.println("  </td>");
                        } else {
                            writer.println("  <td>");
                            generateSimpleGoalButtons(goalEle, name, writer);
                            writer.println("  </td>");
                        }
                    }
                    writer.println("  <td align='right'>");
                    writer.println("    <input type='text' name='score_" + name + "' size='3' align='right' readonly tabindex='-1'>");
                    writer.println("  </td>");
                    formatter.format("  <td class='error' id='error_%s'>&nbsp;</td>%n", name);
                    writer.println("</tr>");
                    writer.println("<!-- end " + name + " -->");
                    writer.newLine();
                } catch (final ParseException pe) {
                    throw new RuntimeException("FATAL: min/max not parsable for goal: " + name);
                }
            }
        }
    }
