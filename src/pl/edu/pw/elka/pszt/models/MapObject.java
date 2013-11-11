package pl.edu.pw.elka.pszt.models;

/**
 * PSZT
 * Created: 10.11.2013 23:38
 */
public abstract class MapObject {
    protected static final String PACKAGE_PREFIX = "pl.edu.pw.elka.pszt.models.";

    @Override
    public String toString() {
        return String.valueOf(this.getClass().getName().replaceFirst(PACKAGE_PREFIX, "").charAt(0));
    }
}
