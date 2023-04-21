    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        JSFUtility.renderScriptOnce(writer, this, context);
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("$(document).ready(function(){\n", null);
        String clientId = getClientId(context);
        clientId = clientId.replace(":", "\\\\:");
        writer.writeText("$(\"#" + clientId + "\").datepicker({", null);
        Map attr = this.getAttributes();
        boolean needed = JSFUtility.writeJSObjectOptions(writer, attr, DatePicker.class);
        if (getAttributes().get(DATEFORMAT) != null) {
            if (needed == true) {
                writer.write(",");
            }
            writer.write(DATEFORMAT + " : '" + convertFormat(getAttributes().get(DATEFORMAT)) + "'");
            needed = true;
        }
        if (getAttributes().get(MINDATE) != null) {
            if (needed == true) {
                writer.write(",");
            }
            String val = (String) getAttributes().get(MINDATE);
            val = val.replace("'", "\\'");
            writer.write(MINDATE + " : (function(){try { return eval('" + val + "'); } catch(e){ return '" + val + "'; }})()");
            needed = true;
        }
        if (getAttributes().get(MAXDATE) != null) {
            if (needed == true) {
                writer.write(",");
            }
            String val = (String) getAttributes().get(MAXDATE);
            val = val.replace("'", "\\'");
            writer.write(MAXDATE + " : (function(){try { return eval('" + val + "'); } catch(e){ return '" + val + "'; }})()");
            needed = true;
        }
        if (getAttributes().get(DAYNAMES) != null) {
            if (needed == true) {
                writer.write(",");
            }
            String val = (String) getAttributes().get(DAYNAMES);
            val = val.replace("'", "\\'");
            writer.write(DAYNAMES + " : (function(){try { return eval('" + val + "'); } catch(e){ return '" + val + "'; }})()");
            needed = true;
        }
        if (getAttributes().get(DAYNAMESMIN) != null) {
            if (needed == true) {
                writer.write(",");
            }
            String val = (String) getAttributes().get(DAYNAMESMIN);
            val = val.replace("'", "\\'");
            writer.write(DAYNAMESMIN + " : (function(){try { return eval('" + val + "'); } catch(e){ return '" + val + "'; }})()");
            needed = true;
        }
        if (getAttributes().get(DAYNAMESSHORT) != null) {
            if (needed == true) {
                writer.write(",");
            }
            String val = (String) getAttributes().get(DAYNAMESSHORT);
            val = val.replace("'", "\\'");
            writer.write(DAYNAMESSHORT + " : (function(){try { return eval('" + val + "'); } catch(e){ return '" + val + "'; }})()");
            needed = true;
        }
        if (getAttributes().get(MONTHNAMES) != null) {
            if (needed == true) {
                writer.write(",");
            }
            String val = (String) getAttributes().get(MONTHNAMES);
            val = val.replace("'", "\\'");
            writer.write(MONTHNAMES + " : (function(){try { return eval('" + val + "'); } catch(e){ return '" + val + "'; }})()");
            needed = true;
        }
        if (getAttributes().get(MONTHNAMESSHORT) != null) {
            if (needed == true) {
                writer.write(",");
            }
            String val = (String) getAttributes().get(MONTHNAMESSHORT);
            val = val.replace("'", "\\'");
            writer.write(MONTHNAMESSHORT + " : (function(){try { return eval('" + val + "'); } catch(e){ return '" + val + "'; }})()");
            needed = true;
        }
        writer.writeText("});\n" + "});", null);
        writer.endElement("script");
    }
