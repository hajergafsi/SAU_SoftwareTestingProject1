/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is responsible for testing the Document class
* </p>
*/

package library;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
class DocumentTest {

	private Document document;
	final String fileRegex = "^.*\\.java$";
	final Pattern pattern = Pattern.compile(fileRegex);
	Matcher matcher;
	Faker faker;
	
	//Test regex expression (should not match any file with extension other .java)
	@ParameterizedTest
	@Tag("ParameterizedTest")
	@DisplayName("Regex test with file extentions other than java")
	@CsvSource({"porro.js","perspiciatis.java.webm","perferendis.odp","voluptates.docx","modi.avi", "vel.jpg"})
	void regexNegativeTest(String name) {
		matcher = pattern.matcher(name);
		assertEquals(matcher.find(),false);
	}
	
	//Test regex expression (should not match any file with extension other .java)
	@ParameterizedTest
	@Tag("ParameterizedTest")
	@DisplayName("Regex test with java files")
	@CsvSource({"porro.java","perspiciatis.class.java","perfer-endis1234.java","voluptates@.java","123modi.java"})
	void regexPositiveTest(String name) {
		matcher = pattern.matcher(name);
		assertEquals(matcher.find(),true);
	}
	
	//Test class behavior when a file with wrong extension is sent (should throw an error)
	@RepeatedTest(10)
	@Tag("RepeatedTest")
	@Tag("RepeatedTest")
	@DisplayName("File extention test")
	void fileExtentionTest() throws Exception {	
		faker = new Faker();
		String fileName = faker.file().fileName(); 
		matcher = pattern.matcher(fileName);
		if(!matcher.find()) {
			Exception exception = assertThrows(Exception.class, () -> {
				document = new Document(fileName);
		    });
			
			String expectedMessage = "File must be a java file !";
		    String actualMessage = exception.getMessage();

		    assertTrue(actualMessage.contains(expectedMessage));
		}
		
	}
	
	//Test with an unexisting file (should throw IOException)
	@Test
	@DisplayName("Inexitant File test")
	void fileReadTest() throws IOException {
		try {
			document = new Document("test.java");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Exception exception = assertThrows(IOException.class, () -> {
			document.read();
	    });
		
		String expectedMessage = "File doesn't exist!";
	    String actualMessage = exception.getMessage();
	    assertEquals(actualMessage,expectedMessage);
	}
	
	//Test with an unexisting file (should throw IOException)
	@Test
	@DisplayName("exitant File test")
	void fileTest(){
		try {
			document = new Document("Main.java");
			document.read();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertDoesNotThrow(() -> document.read());
		
	}
	
	

}
