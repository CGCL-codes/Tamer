package net.sourceforge.wildlife.core.components.actions;

import net.sourceforge.wildlife.core.environment.World;
import net.sourceforge.wildlife.core.types.Action_status;

/**
 * Communicate class
 * 
 * @author Jean Barata
 */
public class Communicate extends Action {

    /**
	 * Communicate class constructor
	 * 
	 * @param world_p
	 */
    public Communicate(World world_p) {
        super(world_p);
    }

    /**
	 * Communicate class constructor
	 * 
	 * @param threadGroup_p
	 * @param world_p
	 */
    public Communicate(ThreadGroup threadGroup_p, World world_p) {
        super(threadGroup_p, world_p);
    }

    /**
	 *
	 */
    @Override
    public void run() {
        set_status(Action_status.TERMINATED);
    }

    /**
	 *
	 */
    @Override
    public String get_type() {
        return "COMMUNICATE";
    }
}
