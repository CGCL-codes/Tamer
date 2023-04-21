package de.iritgo.aktera.shell;

import lombok.Setter;
import javax.inject.Inject;
import de.iritgo.aktera.shell.groovyshell.GroovyShellService;

public class ShellManagerImpl implements ShellManager {

    @Setter
    @Inject
    private GroovyShellService groovyShellService;

    public void startShell() {
        groovyShellService.launch();
    }
}
