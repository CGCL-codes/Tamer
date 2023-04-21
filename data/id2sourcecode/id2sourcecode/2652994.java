    protected void writeHeader(String sPackage, String sClassName, String sBaseName, StringBuffer sbInitialPart) throws IOException {
        writeAllJava("// This file is auto-generated; changes are futile\n\n");
        writeAllJava("package " + sPackage + ";\n\n");
        fwEnterpriseBean.write("import javax.ejb.CreateException;\n");
        fwHomeInterface.write("import java.rmi.RemoteException;\n" + "import javax.ejb.*;\n");
        fwRemoteInterface.write("import java.rmi.RemoteException;\n");
        writeAllJava(sbInitialPart.toString());
        if (iEJBType == ENTITY_BEAN) {
            fwDataContainer.write("public class " + sClassName + "Data extends " + sBaseName + "Data {\n");
        }
        fwEnterpriseBean.write("public class " + sClassName + "EJB extends " + sBaseName);
        switch(iEJBType) {
            case ENTITY_BEAN:
                fwEnterpriseBean.write("EntityBean implements " + sBaseName + "Business {\n");
                break;
            case SESSION_BEAN:
                fwEnterpriseBean.write("Adapter {\n");
                break;
        }
        fwHomeInterface.write("public interface " + sClassName + "Home extends " + sBaseName + "Home {\n");
        fwRemoteInterface.write("public interface " + sClassName + " extends ");
        switch(iEJBType) {
            case ENTITY_BEAN:
                fwRemoteInterface.write(sBaseName + "Business, javax.ejb.EJBObject {\n");
                break;
            case SESSION_BEAN:
                fwRemoteInterface.write("javax.ejb.EJBObject {\n");
                break;
        }
        switch(iEJBType) {
            case ENTITY_BEAN:
                fwEJBFieldXML.write("    <entity>\n");
                break;
            case SESSION_BEAN:
                fwEJBFieldXML.write("    <session>\n");
                break;
        }
        fwEJBFieldXML.write("      <description>" + sClassName + "</description>\n" + "      <display-name>" + sClassName + "</display-name>\n" + "      <ejb-name>" + sClassName + "</ejb-name>\n" + "      <home>" + sPackage + "." + sClassName + "Home</home>\n" + "      <remote>" + sPackage + "." + sClassName + "</remote>\n" + "      <ejb-class>" + sPackage + "." + sClassName + "EJB</ejb-class>\n");
        switch(iEJBType) {
            case ENTITY_BEAN:
                if (sBaseName.endsWith("Node")) {
                    fwEJBFieldXML.write("      <persistence-type>Container</persistence-type>\n" + "      <prim-key-class>java.lang.Integer</prim-key-class>\n" + "      <reentrant>false</reentrant>\n");
                } else {
                    fwEJBFieldXML.write("      <persistence-type>Container</persistence-type>\n" + "      <prim-key-class>org.jboke.framework.ejb.NodeBusiness</prim-key-class>\n" + "      <reentrant>false</reentrant>\n");
                }
                copyResourceInFile(("/" + sBaseName + "-field-ejb-jar").replace('.', '/') + ".xml", fwEJBFieldXML);
                fwJawsXML.write("    <entity>\n" + "      <ejb-name>" + sClassName + "</ejb-name>\n" + "      <create-table>true</create-table>\n" + "      <remove-table>false</remove-table>\n" + "      <tuned-updates>true</tuned-updates>\n" + "      <read-only>false</read-only>\n" + "      <timeout>false</timeout>\n");
                copyResourceInFile(("/" + sBaseName + "-jaws").replace('.', '/') + ".xml", fwJawsXML);
                break;
            case SESSION_BEAN:
                fwEJBFieldXML.write("      <session-type>Stateful</session-type>\n" + "      <transaction-type>Container</transaction-type>\n");
                break;
        }
    }
