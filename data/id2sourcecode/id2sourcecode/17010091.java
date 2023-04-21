    public static void addContents(java.util.Enumeration contents, java.io.PrintWriter writer) {
        org.openorb.compiler.idl.reflect.idlObject obj;
        while (contents.hasMoreElements()) {
            obj = (org.openorb.compiler.idl.reflect.idlObject) contents.nextElement();
            switch(obj.idlType()) {
                case org.openorb.compiler.idl.reflect.idlType.ATTRIBUTE:
                    if (((idlAttribute) obj).isReadOnly()) {
                        writer.println("readonly attribute ");
                    } else {
                        writer.println("attribute ");
                    }
                    writer.println(obj.idlName() + ";<br><br>");
                    break;
                case org.openorb.compiler.idl.reflect.idlType.MODULE:
                    if (obj.idlName().equals("CORBA")) {
                        break;
                    }
                    writer.println("module ");
                    writer.println("<font color=blue>" + obj.idlName() + "</font>{<br><br>");
                    addContents(obj.content(), writer);
                    writer.println("<br>};<br><br>");
                    break;
                case org.openorb.compiler.idl.reflect.idlType.INTERFACE:
                    writer.println("interface ");
                    writer.println("<font color=red>" + obj.idlName() + "</font>{<br><br>");
                    addContents(obj.content(), writer);
                    writer.println("<br>};<br><br>");
                    break;
                case org.openorb.compiler.idl.reflect.idlType.OPERATION:
                    if (((idlOperation) obj).isOneway()) {
                        writer.println("oneway ");
                    }
                    writer.println("void <font color=green>" + obj.idlName() + "</font>();<br><br>");
                    break;
                default:
                    break;
            }
        }
    }
