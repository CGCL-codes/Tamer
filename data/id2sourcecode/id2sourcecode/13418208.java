    private static void generateSimpleGoalButtons(final Element goalEle, final String name, final JspWriter writer) throws IOException, ParseException {
        writer.println("    <table border='0' cellpadding='0' cellspacing='0' width='150'>");
        writer.println("      <tr align='center'>");
        final double min = Utilities.NUMBER_FORMAT_INSTANCE.parse(goalEle.getAttribute("min")).doubleValue();
        final double max = Utilities.NUMBER_FORMAT_INSTANCE.parse(goalEle.getAttribute("max")).doubleValue();
        if (0 == min && 1 == max) {
            generateYesNoButtons(name, writer);
        } else {
            final double range = max - min;
            if (range >= 10) {
                generateIncDecButton(name, 5, writer);
            } else if (range >= 5) {
                generateIncDecButton(name, 3, writer);
            }
            generateIncDecButton(name, 1, writer);
            generateIncDecButton(name, -1, writer);
            if (range >= 10) {
                generateIncDecButton(name, -5, writer);
            } else if (range >= 5) {
                generateIncDecButton(name, -3, writer);
            }
        }
        writer.println("       </tr>");
        writer.println("    </table>");
        writer.println("  </td>");
        writer.println("  <td align='right'>");
        if (0 == min && 1 == max) {
            writer.println("    <input type='text' name='" + name + "_radioValue' size='3' align='right' readonly tabindex='-1'>");
        } else {
            writer.println("    <input type='text' name='" + name + "' size='3' align='right' onChange='" + getCheckMethodName(name) + "()'>");
        }
    }
