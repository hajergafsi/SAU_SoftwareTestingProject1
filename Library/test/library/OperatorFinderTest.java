/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is responsible for testing the OperatorFinder class
* </p>
*/



package library;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.suite.api.IncludeTags;

import com.github.javafaker.Faker;

class OperatorFinderTest {

	private OperatorFinder OF;
	
	@DisplayName("Test Incrementation detector function")
	@ParameterizedTest
	@CsvSource(value={"a++ = kk - 90:operator = kk - 90","int c = a * b++;:int c = a * operator;"},delimiter= ':')
	void IncrementingOperatorsTest(String text,String result) {
		OF = new OperatorFinder(EOperator.numerical,text);
		OF.detectIncrementingOperators();
		assertEquals(OF.getText(),result);
	}
	
	// testing format "a = --i"
	@RepeatedTest(10)
	@Tag("RepeatedTest")
	@Tag("FakerTest")
	@Tag("ParameterizedTest")
	@DisplayName("Test Incrementation operations with faker")
	void TestFaker() {
		Faker faker = new Faker();
		final String operandIncrementRegex = "(((\\w\\w)+(\\+\\+|\\-\\-))|((\\+\\+|\\-\\-)(\\w\\w)+))";
		String code = faker.regexify(operandIncrementRegex + "(\\+|-|\\*|\\/|%|&|\\||\\^|=|\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=)" + operandIncrementRegex);
		OF = new OperatorFinder(EOperator.numerical,code);
		int result = OF.FindOperators();
		assertEquals(result,3);
	}
	
	//Integration with ambiguous string remover
	@RepeatedTest(10)
	@Tag("RepeatedTest")
	@Tag("IntegrationTest")
	@Tag("FakerTest")
	@DisplayName("Test logical operators")
	void TestLogical() {
		AmbiguousStringRemover asr;
		Faker faker = new Faker();
		final String operandRegex = "[\\(]*(_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(((\\w|\\w\\.\\w)*|\\\"(_*([A-Za-z0-9]|__)+(\\.\\w|\\w)*\\s*(\\+|-|\\*|\\/|%|&|\\||\\^|=|\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||!|\\<|\\<=|\\>|\\>=|==|!=)\\s*(_*([A-Za-z0-9]|__)+(\\.\\w|\\w)*)\\\"))*\\))*)";
		asr = new AmbiguousStringRemover(faker.regexify(operandRegex));
		String op1 = asr.replaceAmbiguous(); 
		asr = new AmbiguousStringRemover(faker.regexify(operandRegex));
		String op2 = asr.replaceAmbiguous(); 
		String code = faker.regexify("\s*(\\&\\&|\\|\\||!)\s*");
		OF = new OperatorFinder(EOperator.logical,op1+code+op2);
		int result = OF.FindOperators();
		assertEquals(result,1);
	}
	
	@RepeatedTest(10)
	@Tag("FakerTest")
	@Tag("RepeatedTest")
	@DisplayName("Test relational operators")
	void TestRelational() {
		Faker faker = new Faker();
		final String operandRegex =  "[\\(]*(_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(((\\w|\\w\\.\\w)*|\\\"[^\\\"]\\\")*\\))*|\\\"[^\\\"]*\\\")";
		String operand1 = faker.regexify(operandRegex);
		String operand2 = faker.regexify(operandRegex);
		String code = faker.regexify("\s*(\\<=|\\>=|==|!=|\\<|\\>)\s*");
		OF = new OperatorFinder(EOperator.relational,operand1+code+operand2);
		int result = OF.FindOperators();
		assertEquals(result,1);
	}
	
	@RepeatedTest(10)
	@Tag("IntegrationTest")
	@Tag("FakerTest")
	@Tag("RepeatedTest")
	@DisplayName("Test single operators")
	void TestSingle() {
		AmbiguousStringRemover asr;
		Faker faker = new Faker();
		final String operandRegex = "[\\(]*(_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(((\\w|\\w\\.\\w)*|\\\"(_*([A-Za-z0-9]|__)+(\\.\\w|\\w)*\\s*(\\+|-|\\*|\\/|%|&|\\||\\^|=|\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||!|\\<|\\<=|\\>|\\>=|==|!=)\\s*(_*([A-Za-z0-9]|__)+(\\.\\w|\\w)*)\\\"))*\\))*)";
		asr = new AmbiguousStringRemover(faker.regexify(operandRegex));
		String op1 = asr.replaceAmbiguous(); 
		asr = new AmbiguousStringRemover(faker.regexify(operandRegex));
		String op2 = asr.replaceAmbiguous(); 
		String code = faker.regexify("\s*(\\+|\\-|\\*|\\/|%|&|\\||\\^|=|!|\\<|\\>)\s*");
		OF = new OperatorFinder(EOperator.single,op1+code+op2);
		int result = OF.FindOperators();
		assertEquals(result,1);
	}
	
	@RepeatedTest(10)
	@DisplayName("Test double operators")
	@Tag("FakerTest")
	@Tag("RepeatedTest")
	void TestDouble() {
		AmbiguousStringRemover asr;
		Faker faker = new Faker();
		final String operandRegex =  "[\\(]*(_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(((\\w|\\w\\.\\w)*|\\\"[^\\\"]\\\")*\\))*|\\\"[^\\\"]*\\\")";
		asr = new AmbiguousStringRemover(faker.regexify(operandRegex));
		String op1 = asr.replaceAmbiguous(); 
		asr = new AmbiguousStringRemover(faker.regexify(operandRegex));
		String op2 = asr.replaceAmbiguous(); 
		String code = faker.regexify("\s*(\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||\\<=|\\>=|==|!=)\s*");
		OF = new OperatorFinder(EOperator.doubleOp,op1+code+op2);
		int result = OF.FindOperators();
		assertEquals(result,1);
	}
	
	@RepeatedTest(10)
	@Tag("FakerTest")
	@Tag("RepeatedTest")
	@DisplayName("Test double operators with Incrementation operations")
	void TestDouble2() {
		Faker faker = new Faker();
		final String operandIncrementRegex = "(((\\w\\w)+(\\+\\+|\\-\\-))|((\\+\\+|\\-\\-)(\\w\\w)+))";
		String code = faker.regexify(operandIncrementRegex + "\s*(\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||\\<=|\\>=|==|!=)\s*" + operandIncrementRegex);
		OF = new OperatorFinder(EOperator.doubleOp,code);
		int result = OF.FindOperators();
		assertEquals(result,3);
	}
}
