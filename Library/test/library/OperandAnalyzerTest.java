/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is responsible for testing the OperandAnalyzer class
* </p>
*/


package library;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import com.github.javafaker.Faker;



class OperandAnalyzerTest {

	private OperandAnalyzer OA; 
	interface IOperand {
		public String getOperand();
	}
	
	interface IOperator {
		public String getOperator();
	}
	
	String generateOperator(String operandRegex) {
		return (new Faker()).regexify(operandRegex);
	}
	
	//Test with ambiguous strings as function parameters 
	@ParameterizedTest
	@CsvSource({"2","3","10","4"})
	@Tag("ParameterizedTest")
	@Tag("MockitoTest")
	@Tag("FakerTest")
	@DisplayName("Test with faker generated operands and operators")
	void test(int count) {
		final String operandRegex = "[\\(]*(_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(((\\w|\\w\\.\\w)*|\\\"(_*([A-Za-z0-9]|__)+(\\.\\w|\\w)*\\s*(\\+|-|\\*|\\/|%|&|\\||\\^|=|\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||!|\\<|\\<=|\\>|\\>=|==|!=)\\s*(_*([A-Za-z0-9]|__)+(\\.\\w|\\w)*)\\\")|,)*\\))*)";
		final String operatorRegex = "\s*(\\+|\\-|\\*|\\/|%|&|\\||\\^|=|!|\\<|\\>|\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||\\<=|\\>=|==|!=)\s*";
		IOperand operand = Mockito.mock(IOperand.class);
		IOperator operator = Mockito.mock(IOperator.class);
		Mockito.when(operand.getOperand()).thenReturn((new AmbiguousStringRemover(generateOperator(operandRegex)).replaceAmbiguous()));
		Mockito.when(operator.getOperator()).thenReturn((new Faker()).regexify(operatorRegex));
		String exp = operand.getOperand();
		for(int i=0;i<count-1;i++) {
			exp += operator.getOperator() + operand.getOperand();
		}
		exp += ';';
		OA = new OperandAnalyzer(exp);
		assertEquals(OA.Analyze(),count);
	}
	
	@RepeatedTest(10)
	@Tag("MockitoTest")
	@Tag("RepeatedTest")
	@DisplayName("Test using Randomize and list of operands")
	void repeatedTest() {		
		final String[] operands = new String[] {"A_class.func1(\"a+b\")","func2(a,b,c)","__.num(\"Hey\").a","abc123.__(\"param1\",2).func1(1,\"1==2\")"};	
		final String operatorRegex = "\s*(\\+|\\-|\\*|\\/|%|&|\\||\\^|=|!|\\<|\\>|\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||\\<=|\\>=|==|!=)\s*";
		IOperand operand = Mockito.mock(IOperand.class);
		IOperator operator = Mockito.mock(IOperator.class);
		Mockito.when(operand.getOperand()).thenReturn((new AmbiguousStringRemover(operands[(new Random()).nextInt(operands.length)]).replaceAmbiguous()));
		Mockito.when(operator.getOperator()).thenReturn((new Faker()).regexify(operatorRegex));	
		String exp = operand.getOperand() + operator.getOperator() + operand.getOperand()+ ';';
		OA = new OperandAnalyzer(exp);
		assertEquals(OA.Analyze(),2);
	}
	
	@RepeatedTest(10)
	@Tag("RepeatedTest")
	@Tag("MockitoTest")
	@Tag("FakerTest")
	@DisplayName("Test Incrementation operations")
	void TestFaker() {
		Faker faker = new Faker();
		final String operandRegex = "[\\(]*(_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(((\\w|\\w\\.\\w)*|\\\"(_*([A-Za-z0-9]|__)+(\\.\\w|\\w)*\\s*(\\+|-|\\*|\\/|%|&|\\||\\^|=|\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||!|\\<|\\<=|\\>|\\>=|==|!=)\\s*(_*([A-Za-z0-9]|__)+(\\.\\w|\\w)*)\\\")|,)*\\))*)";
		final String operandIncrementRegex = "((_*([A-Za-z0-9]|__)+\\w*(\\+\\+|\\-\\-))|((\\+\\+|\\-\\-)\s*_*([A-Za-z0-9]|__)+\\w*))";
		final String operatorRegex = "\s*(\\+|\\-|\\*|\\/|%|&|\\||\\^|=|!|\\<|\\>|\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||\\<=|\\>=|==|!=)\s*";
		IOperand operand = Mockito.mock(IOperand.class);
		IOperand operandWithIncrement = Mockito.mock(IOperand.class);
		IOperator operator = Mockito.mock(IOperator.class);
		Mockito.when(operand.getOperand()).thenReturn((new AmbiguousStringRemover(generateOperator(operandRegex)).replaceAmbiguous()));
		Mockito.when(operandWithIncrement.getOperand()).thenReturn(generateOperator(operandIncrementRegex));
		Mockito.when(operator.getOperator()).thenReturn((new Faker()).regexify(operatorRegex));
		//a == ++i
		String exp = operand.getOperand() + operator.getOperator() + operandWithIncrement.getOperand()+ ';';
		//a-- == i
		exp +=  operandWithIncrement.getOperand() + operator.getOperator() + operand.getOperand()+ ';';
		//a-- == ++i
		exp +=  operandWithIncrement.getOperand() + operator.getOperator() +" ++" + operand.getOperand()+ ';';		
		OA = new OperandAnalyzer(exp);
		assertEquals(OA.Analyze(),10);
	}
	
	
	//Integration with Document class
	@Test
	@DisplayName("Testing operands in a java file")
	@Tag("IntegrationTest")
	void DocumentTest() {
		Document document;
		try {
			document = new Document("Main.java");
			document.read();
			document.readAndCleanString();
			document.AnalyzeOperands();
			assertEquals(document.getNumberOfOperands(),43);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@ParameterizedTest
	@Tag("ParameterizedTest")
	@DisplayName("Tests with brackets")
	@CsvSource(value={"int a = array[5] - 10;:3","number[getLength(\"word\")] = 2;:2","result = array[index]--;:3","int i = array[1]++ - 10;:4","System.out.print(array[index++]);:1","System.out.print(array[index++ + arr[2]]);:3"},delimiter=':')
	void testWithArrays(String code,int count) {
		OA = new OperandAnalyzer(code);
		assertEquals(OA.Analyze(),count);
	}	
}
