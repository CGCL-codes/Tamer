    public void render(PrintWriter out) {
        out.write("<div class=\"divrep_date\" id=\"" + getNodeID() + "\">");
        if (!hidden) {
            if (label != null) {
                out.print("<label>" + StringEscapeUtils.escapeHtml(label) + "</label><br/>");
            }
            String str = df_incoming.format(value);
            out.write("<input type=\"text\" class=\"datepicker\" value=\"" + str + "\" onchange=\"divrep('" + getNodeID() + "', null, $(this).val());\"/>");
            out.write("<script type=\"text/javascript\">");
            out.write("$(document).ready(function() { $(\"#" + getNodeID() + " .datepicker\").datepicker({" + "onSelect: function(value) {divrep('" + getNodeID() + "', null, value, 'select');}," + "dateFormat: '" + jquery_format + "'," + "showOn: 'button'," + "beforeShow: function() {$('#ui-datepicker-div').maxZIndex(); }," + "changeYear: true," + "constrainInput: false," + "defaultDate: new Date(" + value.getTime() + ")," + "changeMonth: true");
            out.write("});});");
            out.write("</script>");
            error.render(out);
        }
        out.write("</div>");
    }
