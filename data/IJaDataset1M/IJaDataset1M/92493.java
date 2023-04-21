package abono.dao;

import java.util.*;
import abono.*;

public interface AbonoDAO {

    public int insertar(AbonoVO abono);

    public int eliminar(AbonoVO abono);

    public int actualizar(AbonoVO abono);

    public ArrayList<AbonoVO> listadoAbonos(AbonoParametros parametros);
}
