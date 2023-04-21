/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is responsible for testing the AmbiguousStringRemover class
* </p>
*/

package library;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.github.javafaker.Code;
import com.github.javafaker.Faker;
import com.github.javafaker.ProgrammingLanguage;

class AmbiguousStringRemoverTest {

	private AmbiguousStringRemover AMR;
	
	@ParameterizedTest
	@Tag("ParameterizedTest")
	@CsvSource(value={"\"a=b-c+(24-h);\" + 24 = 250:param + 24 = 250","\"int a = array[5] - 10;\":param","\"number[getLength(\"word\")] = 2;\":param","\"result = array[index]--;\":param","System.out.print(\"array[index++]\");:System.out.print(param);","System.out.print(\"array[ index++ + arr[2 ]]\");:System.out.print(param);"},delimiter=':')
	void simpleOperationTest(String text,String expected) {
		AMR = new AmbiguousStringRemover(text);
		String result = AMR.replaceAmbiguous();
		assertEquals(expected,result);
	}
	
	@ParameterizedTest
	@Tag("ParameterizedTest")
	@DisplayName("moderate Complexity Operation Test")
	@CsvSource(value={"func(\"a+b\",\"test == true\" ) = true:func(param,param ) = true","foo(\"a==b\",\"__test++\"):foo(param,param)"},delimiter= ':')
	void moderateComplexityOperationTest(String text,String expected) {
		AMR = new AmbiguousStringRemover(text);
		String result = AMR.replaceAmbiguous();
		assertEquals(expected,result);
	}
	
	// testing format "c = a+b"
	@RepeatedTest(10)
	@DisplayName("Test operations Inside String")
	void TestWithFaker() {
		Faker faker = new Faker();
		String code = faker.regexify("\\\"_*([A-Za-z0-9]|__)+(\\.\\w|\\w)*\\s*(\\s*(\\+|\\+\\+|-|--|\\*|\\/|%|&|\\||\\^|=|\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||!|==|!=)\\s*_*([A-Za-z0-9]|__)+(\\.\\w|\\w))+\\s*\\\"");
		AMR = new AmbiguousStringRemover(code);
		String result = AMR.replaceAmbiguous();
		assertEquals(result,"param");
	}
	
	// testing format "--i"
	@RepeatedTest(10)
	@Tag("RepeatedTest")
	@DisplayName("Test Incrementation Inside String")
	void TestFaker2() {
		Faker faker = new Faker();
		String code = faker.regexify("\\\"(((\\w\\w)+(\\+\\+|\\-\\-))|((\\+\\+|\\-\\-)(\\w\\w)+))\\\"");
		AMR = new AmbiguousStringRemover(code);
		String result = AMR.replaceAmbiguous();
		assertEquals(result,"param");
	}
	
}