package net.sf.mpango.game.core.dto;

import java.util.List;
import net.sf.mpango.common.dto.BaseDTO;
import net.sf.mpango.common.directory.dto.UserDTO;
import net.sf.mpango.common.directory.enums.StateEnum;

/**
 * data transfer object representing player
 * 
 * @author aplause
 * 
 */
public class PlayerDTO extends BaseDTO {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6825923397531823816L;

    private String name;

    private PositionDTO position;

    private List<UnitDTO> units;

    private StateEnum state;

    private UserDTO user;

    /**
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return
	 */
    public PositionDTO getPosition() {
        return position;
    }

    /**
	 * @param position
	 */
    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    /**
	 * @return
	 */
    public List<UnitDTO> getUnits() {
        return units;
    }

    /**
	 * @param units
	 */
    public void setUnits(List<UnitDTO> units) {
        this.units = units;
    }

    /**
	 * @return
	 */
    public StateEnum getState() {
        return state;
    }

    /**
	 * @param state
	 */
    public void setState(StateEnum state) {
        this.state = state;
    }

    /**
	 * @return
	 */
    public UserDTO getUser() {
        return user;
    }

    /**
	 * @param user
	 */
    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PlayerDTO other = (PlayerDTO) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (user == null) {
            if (other.user != null) return false;
        } else if (!user.equals(other.user)) return false;
        return true;
    }
}
