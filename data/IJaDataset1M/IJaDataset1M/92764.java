package org.gamegineer.client.internal.ui.console.commandlets.server;

import java.util.Arrays;
import java.util.Collections;
import org.gamegineer.client.ui.console.commandlet.AbstractCommandletTestCase;
import org.gamegineer.client.ui.console.commandlet.CommandletException;
import org.gamegineer.client.ui.console.commandlet.ICommandlet;
import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.client.internal.ui.console.commandlets.server.GetGameCommandlet}
 * class to ensure it does not violate the contract of the
 * {@link org.gamegineer.client.ui.console.commandlet.ICommandlet} interface.
 */
public final class GetGameCommandletAsCommandletTest extends AbstractCommandletTestCase {

    /**
     * Initializes a new instance of the
     * {@code GetGameCommandletAsCommandletTest} class.
     */
    public GetGameCommandletAsCommandletTest() {
        super();
    }

    @Override
    protected ICommandlet createCommandlet() {
        return new GetGameCommandlet();
    }

    /**
     * Ensures the {@code execute} method throws an exception when passed an
     * argument list whose size is equal to the minimum illegal value.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = CommandletException.class)
    public void testExecute_ArgCount_MinIllegalValue() throws Exception {
        getCommandlet().execute(createCommandletContext(Arrays.asList("1", "2")));
    }

    /**
     * Ensures the {@code execute} method throws an exception when passed an
     * empty argument list.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = CommandletException.class)
    public void testExecute_ArgCount_Zero() throws Exception {
        getCommandlet().execute(createCommandletContext(Collections.<String>emptyList()));
    }
}
