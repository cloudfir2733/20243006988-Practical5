package com.example.practicalfive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Unit tests for {@link TextProcessor}. They run on the local JVM, so the validation rules can be
 * checked without an emulator: {@code ./gradlew testDebugUnitTest}.
 */
public class TextProcessorTest {

    @Test
    public void acceptsCorrectlyCapitalisedNames() {
        assertEquals("Beijing", TextProcessor.processMunicipality("Beijing"));
        assertEquals("Shanghai", TextProcessor.processMunicipality("Shanghai"));
        assertEquals("Tianjin", TextProcessor.processMunicipality("Tianjin"));
        assertEquals("Chongqing", TextProcessor.processMunicipality("Chongqing"));
    }

    @Test
    public void normalisesAnyCase() {
        assertEquals("Beijing", TextProcessor.processMunicipality("beijing"));
        assertEquals("Beijing", TextProcessor.processMunicipality("BEIJING"));
        assertEquals("Shanghai", TextProcessor.processMunicipality("SHANGHAI"));
        assertEquals("Tianjin", TextProcessor.processMunicipality("tianjin"));
        assertEquals("Chongqing", TextProcessor.processMunicipality("ChOnGqInG"));
    }

    @Test
    public void ignoresSurroundingWhitespace() {
        assertEquals("Beijing", TextProcessor.processMunicipality("  beijing  "));
        assertEquals("Chongqing", TextProcessor.processMunicipality("\tChongqing\n"));
    }

    @Test
    public void rejectsUnsupportedNames() {
        assertNull(TextProcessor.processMunicipality("Guangzhou"));
        assertNull(TextProcessor.processMunicipality("Shenzhen"));
        assertNull(TextProcessor.processMunicipality("Beijing Guangzhou"));
    }

    @Test
    public void rejectsEmptyAndBlankInput() {
        assertNull(TextProcessor.processMunicipality(null));
        assertNull(TextProcessor.processMunicipality(""));
        assertNull(TextProcessor.processMunicipality("   "));
        assertNull(TextProcessor.processMunicipality("\t\n"));
    }

    @Test
    public void mapsNamesOntoTheArrayOrder() {
        assertEquals(0, TextProcessor.indexOfMunicipality("beijing"));
        assertEquals(1, TextProcessor.indexOfMunicipality("SHANGHAI"));
        assertEquals(2, TextProcessor.indexOfMunicipality("chongqing"));
        assertEquals(3, TextProcessor.indexOfMunicipality("Tianjin"));
        assertEquals(-1, TextProcessor.indexOfMunicipality("Guangzhou"));
    }
}
