package pl.edu.pw.elka.pszt.models;

/**
 * PSZT
 * Created: 11.11.2013 00:44
 */
public class Level {

    protected int number;
    protected Map map;

    public Level(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
