package Javalette.Absyn;

public class EComp3 extends Exp implements java.io.Serializable {

    public Exp exp_1, exp_2;

    public EComp3(Exp p1, Exp p2) {
        exp_1 = p1;
        exp_2 = p2;
    }

    public <R, A> R accept(Javalette.Absyn.Exp.Visitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
