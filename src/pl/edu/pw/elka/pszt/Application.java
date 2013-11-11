package pl.edu.pw.elka.pszt;

import pl.edu.pw.elka.pszt.models.Level;
import pl.edu.pw.elka.pszt.utils.LevelFactory;

/**
 * PSZT
 * Created: 11.11.2013 00:33
 */
public class Application {
    /**
     * Main method
     *
     * @param args app arguments
     */
    public static void main(String[] args) {
        try {
            Level level1 = LevelFactory.createFromProperties("level1");
            System.out.println(level1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
