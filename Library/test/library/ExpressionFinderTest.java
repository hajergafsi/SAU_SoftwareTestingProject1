/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is responsible for testing the ExpressionFinder class
* </p>
*/

package library;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.suite.api.IncludeTags;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.javafaker.Faker;

import library.CommentFinderTest.IGenerator;


@ExtendWith(MockitoExtension.class)
class ExpressionFinderTest {
	
	interface IGenerator {
		public String generate();
		public String generateWithCount(int count);
 	}

	private ExpressionFinder EF;
	
	//Integration with operatorFinder
	@ParameterizedTest
	@Order(1)
	@Tag("ParameterizedTest")
	@CsvSource(value={"(e.hash == hash && ((k = e.key) == key || key.equals(k))) {:5","b = j ++ - -- k;:4","int a = array[5] - 10;:2","number[getLength(\"word\")] = 2;:1","result = array[index]--;:2","System.out.print(array[index++]);:1","System.out.print(array[index++ + arr[2]]);:2"},delimiter=':')
	@DisplayName("Test that calculates total number of operators")
	void CalculateTotalOpsTest(String text,int count) {
		EF = new ExpressionFinder(text);
		assertEquals(count,EF.Analyze(EOperator.numerical)+EF.Analyze(EOperator.logical)+EF.Analyze(EOperator.relational));
	}
	
	//Integration with Document class
	@Test
	@Tag("IntegrationTest")
	@Order(3)
	@DisplayName("Testing numerical opertaors in a java file")
	void DocumentTest() {
		Document document;
		try {
			document = new Document("Main.java");
			document.read();
			document.readAndCleanString();
			document.AnalyzeOperators();
			assertEquals(document.getNumberOfNumericOperators(),20);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private String createExp(int count) {
		IGenerator oprandGenerator = Mockito.mock(IGenerator.class);
		IGenerator operatorGenerator = Mockito.mock(IGenerator.class);
		final String operatorRegex = "\s*(\\+|\\-|\\*|\\/|%|&|\\||\\^|=|!|\\<|\\>|\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||\\<=|\\>=|==|!=)\s*";
		Mockito.when(operatorGenerator.generate()).thenReturn((new Faker()).regexify(operatorRegex));
		Mockito.when(oprandGenerator.generate()).thenReturn((new Faker()).lorem().sentence(3) + new Faker().lorem().word() + " " );
		String exp = oprandGenerator.generate() ;
		for(int i=0;i<count;i++) {
			exp += (operatorGenerator.generate() + oprandGenerator.generate());
		}
		exp += ';';
		return exp;
	}
	
	@ParameterizedTest
	@Tag("ParameterizedTest")
	@CsvSource({"2","5","1","3","5"})
	@DisplayName("Testing by generating expressions")
	@Tag("MockitoTest")
	@Order(2)
	void TestWithMock(int count) {
		IGenerator expressionGenerator = Mockito.mock(IGenerator.class); 
		String generated = createExp(count);
		Mockito.when(expressionGenerator.generateWithCount(count)).thenReturn(generated);
		EF = new ExpressionFinder(expressionGenerator.generateWithCount(count));
		assertEquals(count,EF.Analyze(EOperator.numerical)+EF.Analyze(EOperator.logical)+EF.Analyze(EOperator.relational));
	}
	
}
