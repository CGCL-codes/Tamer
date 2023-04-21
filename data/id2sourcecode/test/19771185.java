    private void outputJavascript(Class<?> pageClass, PrintWriter writer) {
        writer.println("<script type=\"text/javascript\">");
        {
            writer.println("function doAction(name) {");
            writer.println("\tdocument.ActionForm.ActionName.value = name;");
            writer.println("\tdocument.ActionForm.submit();");
            writer.println("}");
            writer.println();
            writer.println("function doConfirmAction(name, confirmation) {");
            writer.println("\tvar answer = confirm(confirmation);");
            writer.println("\tif (answer) {");
            writer.println("\t\tdocument.ActionForm.ActionName.value = name;");
            writer.println("\t\tdocument.ActionForm.submit();");
            writer.println("\t}");
            writer.println("}");
            writer.println();
            writer.println("function doGlobalAction(name) {");
            writer.println("\tdocument.GlobalActionForm.GlobalActionName.value = name;");
            writer.println("\tdocument.GlobalActionForm.submit();");
            writer.println("}");
            writer.println();
            writer.println("var req = false;");
            writer.println("function doAjaxAction(name) {");
            {
                writer.println("\tif (window.XMLHttpRequest) {");
                {
                    writer.println("\t\ttry {req = new XMLHttpRequest();} catch(e) {}");
                    writer.println("\t} else {");
                    writer.println("\t\ttry {");
                    writer.println("\t\t\treq = new ActiveXObject('Msxml2.XMLHTTP');");
                    writer.println("\t\t} catch(e) {");
                    writer.println("\t\t\ttry {req = new ActiveXObject('Microsoft.XMLHTTP');} catch(e) {}");
                    writer.println("\t\t}");
                }
                writer.println("\t}");
            }
            writer.println("\tif (req) {");
            writer.println("\t\tdocument.getElementById('bimbo.ajax.status').innerHTML = 'Processing...';");
            writer.println("\t\tvar params='AjaxActionName=' + escape(name);");
            writer.println("\t\treq.onreadystatechange = processAjaxResponse;");
            writer.println("\t\treq.open('POST', 'ajax.bimbo', true);");
            writer.println("\t\treq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');");
            writer.println("\t\treq.setRequestHeader('Content-Length', params.length);");
            writer.println("\t\treq.setRequestHeader('Connection', 'close');");
            writer.println("\t\treq.send(params);");
            writer.println("\t}");
            writer.println("}");
            writer.println();
            writer.println("function processAjaxResponse() {");
            writer.println("\tif (req.readyState == 4) {");
            writer.println("\t\tif (req.status == 200) {");
            writer.println("\t\t\tdocument.getElementById('bimbo.ajax.status').innerHTML = '';");
            writer.println("\t\t}");
            writer.println("\t}");
            writer.println("}");
        }
        writer.println("</script>");
    }
