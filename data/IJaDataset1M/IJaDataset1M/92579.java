package com.ilog.translator.java2cs.popup.actions;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.SourceMethod;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.ui.internal.PluginAction;
import org.xml.sax.SAXException;
import com.ilog.translator.java2cs.configuration.GlobalOptions;
import com.ilog.translator.java2cs.configuration.Java2CsModel;
import com.ilog.translator.java2cs.configuration.TranslationConfiguration;
import com.ilog.translator.java2cs.configuration.TranslatorProjectOptions;
import com.ilog.translator.java2cs.plugin.Messages;

@SuppressWarnings("restriction")
public class MigrateJDKMappingFilesAction extends ActionDelegate implements IObjectActionDelegate, IEditorActionDelegate {

    IEditorPart editor = null;

    /**
	 * Constructor for Action1.
	 */
    public MigrateJDKMappingFilesAction() {
        super();
    }

    @Override
    public void run(IAction action) {
        final PluginAction opa = (PluginAction) action;
        if (opa.getSelection() instanceof StructuredSelection) {
            final StructuredSelection selection = (StructuredSelection) opa.getSelection();
            final Object firstElem = selection.getFirstElement();
            if ((firstElem instanceof SourceMethod)) {
                final SourceMethod method = (SourceMethod) firstElem;
                final ICompilationUnit compilationUnit = method.getCompilationUnit();
                migrate(compilationUnit);
            } else if ((firstElem instanceof SourceType)) {
                final SourceType type = (SourceType) firstElem;
                final ICompilationUnit compilationUnit = type.getCompilationUnit();
                migrate(compilationUnit);
            } else if ((firstElem instanceof IJavaProject)) {
                final IJavaProject proj = (IJavaProject) firstElem;
                migrate(proj);
            }
        } else {
            if (editor instanceof CompilationUnitEditor) {
                try {
                    final CompilationUnitEditor cuEditor = (CompilationUnitEditor) editor;
                    final ITextSelection select = (ITextSelection) opa.getSelection();
                    final ICompilationUnit element = (ICompilationUnit) JavaUI.getEditorInputJavaElement(cuEditor.getEditorInput());
                    final IJavaElement selectElement = element.getElementAt(select.getOffset() + select.getLength() / 2);
                    System.out.println();
                } catch (final JavaModelException e) {
                }
            }
        }
    }

    public void migrate(ICompilationUnit compilationUnit) {
        final IJavaProject javaProject = compilationUnit.getJavaProject();
        migrate(javaProject);
    }

    public void migrate(IJavaProject javaProject) {
        try {
            final GlobalOptions globalOptions = new GlobalOptions();
            migrateMappingFiles(javaProject, globalOptions);
        } catch (final Exception e) {
            System.err.println(Messages.getString("ProjectTransferHandler.error_copying_projects") + e);
        }
    }

    private static void migrateMappingFiles(IJavaProject javaProject, GlobalOptions globalOptions) throws ParserConfigurationException, SAXException, JavaModelException, IOException, CoreException {
        final String translatorDirectory = TranslatorProjectOptions.searchTranslatorDir(javaProject, globalOptions, true, true, null);
        final String confFileName = TranslatorProjectOptions.getOrCreateTranslatorConfigurationFile(javaProject, globalOptions, true, translatorDirectory, true);
        final TranslatorProjectOptions options = new TranslatorProjectOptions(confFileName, globalOptions);
        options.read(javaProject, false);
        final TranslationConfiguration configuration = new TranslationConfiguration(javaProject, javaProject, options);
        final Java2CsModel model = new Java2CsModel(javaProject, configuration.getLogger(), configuration);
        model.migrateJDKMappingFiles();
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        editor = targetEditor;
    }
}
