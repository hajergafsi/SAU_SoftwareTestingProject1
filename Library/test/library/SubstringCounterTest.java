/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is responsible for testing the SubstringCounter class
* </p>
*/


package library;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SubstringCounterTest {

	private SubstringCounter Sc;
	
	@ParameterizedTest
	@Tag("ParameterizedTest")
	@DisplayName("Simple Test")
	@CsvSource({"a + b = c - d+g,+,2 ","a-(a &&true &&b) ==\"aa\"&&\"aq\",&&,3","true |false\\n|aa|b -c,|,3","q-"
			+ "ee-a=12,-,2"})
	void testCountMatches(String mainText, String subString, int count) {
		Sc = new SubstringCounter(mainText,subString);
		assertEquals(Sc.countMatches(),count);
	}
	
	

}
