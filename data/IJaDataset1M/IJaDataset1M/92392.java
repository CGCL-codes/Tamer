package org.antdepo.tasks.conditions;

import net.sf.antcontrib.platform.ShellScriptTask;
import org.antdepo.Constants;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.condition.Condition;

/**
 * ControlTier Software Inc.
 * User: alexh
 * Date: Dec 8, 2004
 * Time: 8:51:56 AM
 *
 * @version $Revision: 4 $
 */
public class ShellscriptCondition extends ProjectComponent implements Condition {

    private String executable;

    public void setExecutable(final String exe) {
        executable = exe;
    }

    private String shellscript;

    public void addText(final String shellscriptText) {
        shellscript = shellscriptText;
    }

    public boolean eval() {
        final Class cls;
        try {
            cls = Class.forName(Constants.ANTCONTRIB_SHELLTASK_CLSNAME);
        } catch (ClassNotFoundException e) {
            throw new BuildException("Failed loading " + Constants.ANTCONTRIB_SHELLTASK_CLSNAME + " task: ", e);
        }
        getProject().addTaskDefinition("shellscript", cls);
        final ShellScriptTask shelltask = (ShellScriptTask) getProject().createTask("shellscript");
        shelltask.setExecutable(executable);
        shelltask.addText(shellscript);
        shelltask.setResultProperty("ShellscriptCondition.result");
        shelltask.setFailonerror(false);
        shelltask.execute();
        final String result = getProject().getProperty("ShellscriptCondition.result");
        getProject().log("shell result value: " + result, Project.MSG_DEBUG);
        if (null != result) {
            final int i = Integer.parseInt(result);
            return i == 0;
        } else {
            return false;
        }
    }
}
