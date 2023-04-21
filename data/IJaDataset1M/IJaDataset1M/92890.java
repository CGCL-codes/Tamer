package com.google.code.p.keytooliui.shared.util.jarsigner;

import com.google.code.p.keytooliui.shared.lang.*;
import com.google.code.p.keytooliui.shared.swing.optionpane.*;
import java.io.*;
import java.awt.*;

public final class UtilJsrFile {

    private static final String _f_s_strClass = "com.google.code.p.keytooliui.shared.util.jarsigner.UtilJsrFile";

    private static boolean _s_blnFileSaveOverwriteAlways = false;

    /**
        if any code or config error, exiting
        else if any other type of error, show warning/error dialog, then return null
    **/
    public static File s_getFileSave(Frame frmOwner, String strPathAbs, boolean blnShowDlgOverwrite) {
        String strMethod = UtilJsrFile._f_s_strClass + "." + "s_getFileSave(...)";
        if (strPathAbs == null) {
            MySystem.s_printOutExit(strMethod, " nil strPathAbs");
        }
        File fleSave = new File(strPathAbs);
        if (!fleSave.exists()) return fleSave;
        if (!blnShowDlgOverwrite) return fleSave;
        if (UtilJsrFile._s_blnFileSaveOverwriteAlways) return fleSave;
        Object[] objsOptions = { "Cancel", "Overwrite", "Always Overwrite" };
        Object objInitialValue = objsOptions[0];
        String strMessageBody = "Confirm overwrite:";
        strMessageBody += "\n";
        strMessageBody += "  ";
        strMessageBody += fleSave.getAbsolutePath();
        strMessageBody += "\n\n";
        strMessageBody += "Please choose an option below?";
        String strResult = OPAbstract.s_showQuestionInputDialog(frmOwner, strMessageBody, objsOptions, objInitialValue);
        if (strResult == null) {
            MySystem.s_printOutTrace(strMethod, "nil strResult");
            return null;
        }
        strResult = strResult.trim();
        if (strResult.length() < 1) {
            MySystem.s_printOutExit(strMethod, "strResult.length()<1: " + strResult);
        }
        if (strResult.equalsIgnoreCase((String) objsOptions[0])) {
            return null;
        }
        if (strResult.equalsIgnoreCase((String) objsOptions[1])) {
            return fleSave;
        }
        if (strResult.equalsIgnoreCase((String) objsOptions[2])) {
            UtilJsrFile._s_blnFileSaveOverwriteAlways = true;
            return fleSave;
        }
        MySystem.s_printOutExit(strMethod, "uncaught value, strResult=" + strResult);
        return null;
    }

    /**
        if any code or config error, exiting
        else if any other type of error, show warning/error dialog, then return null
    **/
    public static File s_getFileOpen(Frame frmOwner, String strPathAbs) {
        String strMethod = UtilJsrFile._f_s_strClass + "." + "s_getFileOpen(...)";
        if (strPathAbs == null) {
            MySystem.s_printOutExit(strMethod, " nil strPathAbs");
        }
        File fleOpen = new File(strPathAbs);
        if (!fleOpen.exists()) {
            MySystem.s_printOutError(strMethod, "! fleOpen.exists(), fleOpen.getAbsolutePath()=" + fleOpen.getAbsolutePath());
            String strBody = "File not found:";
            strBody += "\n  " + fleOpen.getAbsolutePath();
            OPAbstract.s_showDialogWarning(frmOwner, strBody);
            return null;
        }
        if (!fleOpen.canWrite()) {
            MySystem.s_printOutError(strMethod, "! fleOpen.canWrite(), fleOpen.getAbsolutePath()=" + fleOpen.getAbsolutePath());
            String strBody = "File is write-protected:";
            strBody += "\n  " + fleOpen.getAbsolutePath();
            OPAbstract.s_showDialogWarning(frmOwner, strBody);
            return null;
        }
        if (!fleOpen.isFile()) {
            MySystem.s_printOutError(strMethod, "! fleOpen.isFile(), fleOpen.getAbsolutePath()=" + fleOpen.getAbsolutePath());
            String strBody = "File is directory:";
            strBody += "\n  " + fleOpen.getAbsolutePath();
            OPAbstract.s_showDialogWarning(frmOwner, strBody);
            return null;
        }
        return fleOpen;
    }
}
