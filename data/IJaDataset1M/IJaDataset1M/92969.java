package org.jomper.proxy;

import org.jomper.mana.CoreModel;
import org.jomper.mana.Permissions;

/**
 * @author Jomper.Chow
 * @project 
 * @version 2007-6-9
 * @description 
 */
public interface Proxy {

    public void process(String action, Permissions permissions, CoreModel coreModel);
}
