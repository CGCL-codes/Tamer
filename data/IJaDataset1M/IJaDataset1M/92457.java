package com.coderdream.chapter19.state.a2;

/**
 * @author XL
 * 
 */
public interface Context {

    public abstract void setClock(int hour);

    public abstract void changeState(State state);

    public abstract void callSecurityCenter(String msg);

    public abstract void recordLog(String msg);
}
