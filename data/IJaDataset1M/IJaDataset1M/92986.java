package de.iritgo.aktera.core.container;

import de.iritgo.aktera.core.exception.NestedException;

public class ContainerException extends NestedException {

    public ContainerException(String s) {
        super(s);
    }

    public ContainerException(String s, Throwable t) {
        super(s, t);
    }
}
