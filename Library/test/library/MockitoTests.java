package library;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({OperandAnalyzerTest.class,ExpressionFinderTest.class,CommentFinderTest.class})
@IncludeTags("MockitoTest")
public class MockitoTests {

}
