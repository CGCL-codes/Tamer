    private static void generateEnumeratedGoalButtons(final Element goal, final String goalName, final JspWriter writer) throws IOException, ParseException {
        writer.println("    <table border='0' cellpadding='0' cellspacing='0' width='100%'>");
        for (final Element valueEle : new NodelistElementCollectionAdapter(goal.getElementsByTagName("value"))) {
            final String valueTitle = valueEle.getAttribute("title");
            final String value = valueEle.getAttribute("value");
            writer.println("      <tr>");
            writer.println("        <td>");
            writer.println("          <input type='radio' name='" + goalName + "' value='" + value + "' id='" + getIDForEnumRadio(goalName, value) + "' ' onclick='" + getSetMethodName(goalName) + "(\"" + value + "\")'>");
            writer.println("        </td>");
            writer.println("        <td>");
            writer.println("          " + valueTitle);
            writer.println("        </td>");
            writer.println("      </tr>");
        }
        writer.println("        </table>");
        writer.println("  <td align='right'>");
        writer.println("    <input type='text' name='" + goalName + "_radioValue' size='10' align='right' readonly tabindex='-1'>");
    }
