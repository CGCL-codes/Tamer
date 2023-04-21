package de.ghost23.stubcodegenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ListIterator;
import java.util.Map;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import de.ghost23.stubcodegenerator.model.FileObj;
import de.ghost23.stubcodegenerator.model.PackageObj;
import de.ghost23.stubcodegenerator.recognize.stubcodegrammarParser.prog_return;

public class CreateStubCodeAndFolders {

    private prog_return rootPackage;

    private String targetDirectory;

    private String individualTemplatesPath = "";

    private StringTemplateGroup templateLoader;

    public CreateStubCodeAndFolders(prog_return i_rootPackage, String i_targetDirectory) {
        rootPackage = i_rootPackage;
        targetDirectory = i_targetDirectory;
        templateLoader = new StringTemplateGroup("stubtemplates", "stubcode_templates");
    }

    public CreateStubCodeAndFolders(prog_return i_rootPackage, String i_targetDirectory, String i_individualTemplatesPath) {
        rootPackage = i_rootPackage;
        targetDirectory = i_targetDirectory;
        individualTemplatesPath = i_individualTemplatesPath;
        templateLoader = new StringTemplateGroup("stubtemplates", individualTemplatesPath);
    }

    public void execute() {
        createFilesAndFolders(rootPackage.rootPackage, targetDirectory, null);
    }

    private void createFilesAndFolders(PackageObj i_package, String i_targetDirectory, String packagePath) {
        File tmpFile;
        PackageObj tmpPackage;
        for (ListIterator<PackageObj> i = i_package.childPackages.listIterator(); i.hasNext(); ) {
            System.out.print(".");
            tmpPackage = i.next();
            tmpFile = new File(i_targetDirectory, tmpPackage.name);
            tmpFile.mkdir();
            createFilesAndFolders(tmpPackage, tmpFile.getPath(), packagePath == null ? tmpPackage.name : packagePath + "." + tmpPackage.name);
        }
        FileObj tmpFileObj;
        for (ListIterator<FileObj> j = i_package.childFiles.listIterator(); j.hasNext(); ) {
            System.out.print(".");
            tmpFileObj = j.next();
            StringTemplate tmpTemplate = null;
            try {
                tmpTemplate = templateLoader.getInstanceOf(tmpFileObj.template);
                tmpTemplate.setAttribute("className", tmpFileObj.name);
                tmpTemplate.setAttribute("packagePath", packagePath);
                for (Map.Entry<String, String> de : rootPackage.varStatements.entrySet()) {
                    tmpTemplate.setAttribute(de.getKey(), de.getValue());
                }
            } catch (Exception e) {
                System.err.println("Error: template '" + tmpFileObj.template + "' for '" + tmpFileObj.name + "' not found. Skipping.");
            }
            if (tmpTemplate != null) {
                tmpFile = new File(i_targetDirectory, tmpFileObj.name + ".as");
                try {
                    tmpFile.createNewFile();
                } catch (IOException e) {
                    System.err.println(e);
                }
                Writer fw = null;
                try {
                    fw = new FileWriter(tmpFile);
                    fw.write(tmpTemplate.toString());
                    fw.append('\n');
                } catch (IOException e) {
                    System.err.println("Couldn't create file " + tmpFileObj.name + ".");
                } finally {
                    if (fw != null) try {
                        fw.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        System.out.println(".");
    }
}
