    private void getAlertRuleHtmlView(AlertRule alertRule, StringBuffer sbuff) {
        if (alertRule instanceof SimpleAlertRule) {
            SimpleAlertRule simpleAlertRule = (SimpleAlertRule) alertRule;
            sbuff.append("<b>Components alert rule:<b><br>");
            Map<String, Set<String>> channelRules = simpleAlertRule.getChannelRules();
            if (CollectionUtils.isBlankMap(channelRules)) {
                return;
            }
            sbuff.append("<table border='1' >");
            sbuff.append("<tr bgcolor='#CCFF66'><td>").append("from");
            sbuff.append("</td>").append("<td>to</td></tr>");
            for (Map.Entry<String, Set<String>> entry : channelRules.entrySet()) {
                sbuff.append("<tr><td><b><font color='FF0000'>").append(entry.getKey());
                sbuff.append("</td></font>").append("<td><font color='FF00FF'>");
                for (String to : entry.getValue()) {
                    sbuff.append(to).append("<br>");
                }
                sbuff.append("</font></td></tr>");
            }
            sbuff.append("</table>");
        }
    }
