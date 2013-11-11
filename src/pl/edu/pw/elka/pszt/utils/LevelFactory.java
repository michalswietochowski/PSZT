package pl.edu.pw.elka.pszt.utils;

import pl.edu.pw.elka.pszt.models.Level;
import pl.edu.pw.elka.pszt.models.Location;
import pl.edu.pw.elka.pszt.models.Map;
import pl.edu.pw.elka.pszt.models.MapObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * PSZT
 * Created: 11.11.2013 01:02
 */
public class LevelFactory {
    private static final String RESOURCES_FOLDER = "resources/";
    private static final String PROPERTIES_EXTENSION = ".properties";
    private static final String NUMBER = "number";
    private static final String MAP_WIDTH = "map.width";
    private static final String MAP_HEIGHT = "map.height";
    private static final String LOCATION_FORMAT = "location.%d.%d.";
    private static final String LOCATION_OBJECTS = "objects";
    private static final String MODEL_CLASS_FORMAT = "pl.edu.pw.elka.pszt.models.";

    public static Level createFromProperties(String propertiesFilename) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(RESOURCES_FOLDER + propertiesFilename + PROPERTIES_EXTENSION));

        int levelNumber = Integer.parseInt(properties.getProperty(NUMBER));
        Level level = new Level(levelNumber);

        int mapWidth = Integer.parseInt(properties.getProperty(MAP_WIDTH));
        int mapHeight = Integer.parseInt(properties.getProperty(MAP_HEIGHT));
        Map map = new Map(mapWidth, mapHeight);

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Location location = new Location(x, y);
                Set<MapObject> objects = new HashSet<MapObject>();

                String objCountProp = String.format(LOCATION_FORMAT, y, x) + LOCATION_OBJECTS;
                int objCount = Integer.parseInt(properties.getProperty(objCountProp));

                for (int z = 0; z < objCount; z++) {
                    String objNameProp = String.format(LOCATION_FORMAT, y, x) + z;
                    String objName = properties.getProperty(objNameProp);
                    objects.add((MapObject) Class.forName(MODEL_CLASS_FORMAT + objName).getConstructor().newInstance());
                }
                location.setObjects(objects);
                map.setLocationAt(x, y, location);
            }
        }

        level.setMap(map);

        return level;
    }
}
