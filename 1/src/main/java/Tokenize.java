import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.StreamTokenizer;
import java.util.StringTokenizer;
 
public class Tokenize {
	static int a=1;
	Tokenize(int count)
	{
		a = count;
	}
    public static void main(String[] argv) throws Exception {  
    	PrintStream console = System.out;
    	FileReader fileReader = new FileReader("C:\\Users\\nvuggam\\Desktop\\Difference.txt");
    	File tokens= new File("C:\\Users\\nvuggam\\Desktop\\tokens"+a+".txt");
    	PrintStream hii = new PrintStream(tokens);
    	System.setOut(hii);
    	StreamTokenizer tokenizer = new StreamTokenizer(fileReader);
    	tokenizer.parseNumbers();
    	tokenizer.wordChars('_', '_');
    	tokenizer.eolIsSignificant(false); 
    	tokenizer.ordinaryChars(0, ' ');
    	tokenizer.slashSlashComments(true);
    	tokenizer.slashStarComments(true);
    	int tok = tokenizer.nextToken();
    	while (tok != StreamTokenizer.TT_EOF) {
    		tok = tokenizer.nextToken();
    		switch (tok) {
 
    		case StreamTokenizer.TT_NUMBER:
    			double n = tokenizer.nval;
    			System.out.println(n);
    		break;
 
    		case StreamTokenizer.TT_WORD:
    			String word = tokenizer.sval;
    			StringTokenizer dotTokenizer = new StringTokenizer(word,".");
    			while(dotTokenizer.hasMoreTokens())
				{
					 System.out.println(dotTokenizer.nextToken());
				}
    		break;				 
    		case '"':
				  String doublequote = tokenizer.sval; 
				  System.out.println(doublequote);
			break;
			case StreamTokenizer.TT_EOL:			 
			break;
			case StreamTokenizer.TT_EOF:
				break;
			default:
				char character = (char) tokenizer.ttype;
				if ( !(character==' ' || character=='\t') )
				{
					System.out.println(character);
				}
			break;
    		}
    	}
    	fileReader.close();
    	System.setOut(console);
    }
}
