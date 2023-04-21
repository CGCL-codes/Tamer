package jircbot.utils;

import java.io.*;

/**
 * This is an all purpose configuration file reader, whereby
 * it gives the ability to read basic configuration files
 * 
 * TODO: Sort out bug where getVars() returns null if there is only one value.
 *
 * @author matt
 */
public class ConfigReader {

    private BufferedReader brCfg;

    private String[] arrVariables;

    /**
     * Constructor method
     *
     * @param strFileName The file name of the config file
     */
    public ConfigReader(String strFilePath) throws IOException {
        brCfg = new BufferedReader(new FileReader(strFilePath));
        boolean loop = true;
        String strCfgLine = "";
        String strCfgLines = "";
        while (loop == true) {
            if (strCfgLine != null) {
                strCfgLine = brCfg.readLine();
                strCfgLines += strCfgLine + ':';
            } else {
                strCfgLines = strCfgLines.substring(0, strCfgLines.length() - 1);
                loop = false;
            }
        }
        brCfg.close();
        arrVariables = strCfgLines.split(":");
    }

    /**
     * Retrieves the specifide varible
     * 
     * @param strVarName the variable name
     * @return the value of  the variable
     */
    public String getVar(String strVarName) {
        String name;
        for (int i = 0; i < arrVariables.length - 1; i++) {
            name = arrVariables[i].substring(0, arrVariables[i].indexOf("="));
            if (name.equals(strVarName)) return arrVariables[i].substring(arrVariables[i].indexOf("=") + 1);
        }
        return null;
    }

    /**
     * Retrieves the specified variable from an array of values inside the
     * variable
     * 
     * @param strVarName The name of the variable
     * @param index The index of the array
     * @return The string containing the variable at the specified index
     */
    public String getVar(String strVarName, int index) {
        String value = getVar(strVarName);
        if (value != null && value.indexOf(',') != -1 && (count(value, ',') >= index)) return value.split(",")[index];
        return null;
    }

    /**
     * Retrieves a set of variables as an array of strings
     * 
     * @param strVarName Name of the variable
     * @return Array of variables. Null if none are found
     */
    public String[] getVars(String strVarName) {
        int count = getVarCount(strVarName);
        String[] arrValues;
        if (count > 0) {
            arrValues = new String[count];
            for (int i = 0; i < count; i++) arrValues[i] = getVar(strVarName, i);
        } else {
            return null;
        }
        return arrValues;
    }

    /**
     * Retieves the number of values to a certain variable
     *
     * @param strVarName The variable name
     * @return the number of values to a certain variable
     */
    public int getVarCount(String strVarName) {
        if (getVar(strVarName) != null) return count(getVar(strVarName), ',') + 1;
        return 0;
    }

    /**
     * Counts the number of occurances a character has inside a string
     *
     * @param str The string to check
     * @param chr The character to count
     * @return The number of times charect chr occurs inside the string
     */
    public int count(String str, char chr) {
        int ct = 0;
        for (int i = 0; i < str.length(); i++) if (str.charAt(i) == chr) ct++;
        return ct;
    }
}
