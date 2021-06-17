package com.epam.prejap.tetris;

import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.jar.JarFile;

import static org.testng.Assert.*;

/**
 * Integration tests that check whether JAR archive was correctly created.
 * The class gets correct JAR file name from properties extracted from <b>pom.xml</b> with
 * <b>properties-maven-plugin</b>.
 */
@Test(groups = "jar")
public class JARArchiveIT {
    private static final String MAIN_CLASS_FQN = Tetris.class.getName();
    private static final String PROPERTIES_FILE = "target" + File.separator + "test-classes" +
            File.separator + "properties-from-pom.properties";
    private static final String JAR_PATH_PROPERTY = "test.jar.file.location";

    public void propertiesFileForJarTestsExists() {
        File file = new File(PROPERTIES_FILE);
        assertTrue(file.exists(), "The file with properties does not exist or the path is wrong");
    }

    public void jarFileForTestingIsCreated() throws IOException {
        PropertiesReader reader = new PropertiesReader(PROPERTIES_FILE);
        String jarFilePath = reader.getProperty(JAR_PATH_PROPERTY);
        File file = new File(jarFilePath);
        assertTrue(file.exists(), "The JAR file for tests was not created or the path is wrong");
    }

    public void jarFileForTestingHasManifestFile() throws IOException {
        PropertiesReader reader = new PropertiesReader(PROPERTIES_FILE);
        String jarFilePath = reader.getProperty(JAR_PATH_PROPERTY);
        JarFile file = new JarFile(jarFilePath);
        assertNotNull(file.getManifest(), "The MANIFEST.MF is not present in JAR file");
    }

    public void jarFileForTestingContainsCorrectMainClass() throws IOException {
        PropertiesReader reader = new PropertiesReader(PROPERTIES_FILE);
        String jarFilePath = reader.getProperty(JAR_PATH_PROPERTY);
        JarFile file = new JarFile(jarFilePath);
        String mainClassFQN = file.getManifest()
                .getMainAttributes()
                .getValue("Main-Class");
        assertEquals(mainClassFQN, MAIN_CLASS_FQN,
                "The fully qualified name of the class with method main() is not correct");
    }

    /**
     * Helper class to get correct JAR file name.
     * Eliminates the need to provide exact JAR filename every time the version or project name is changed.
     */
    private class PropertiesReader {
        private final Properties properties;

        PropertiesReader(String filePath) throws IOException {
            InputStream is = new FileInputStream(filePath);
            this.properties = new Properties();
            this.properties.load(is);
        }

        String getProperty(String propertyName) {
            return this.properties.getProperty(propertyName);
        }
    }

}
