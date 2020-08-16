
import java.util.Vector;
import java.util.Stack;

public class Calculator {

    // See https://docs.oracle.com/javase/10/docs/api/java/util/Stack.html
static Stack<Double> numberStack = new Stack<Double>();
static Stack<String> operatorStack = new <String>Stack();
    // See https://docs.oracle.com/javase/10/docs/api/java/lang/String.html
static String operators =  "+-%*/^" ;



public static void main (String args []) {
  performCalculation("1", "*", "{", "2", "+", "3", "-",
                       "[", "1", "*", "(", "2", "-", "1", ")", "]", "+", "3", "}");
  performCalculation("2", "+", "[", "(", "6", "-", "3", ")", "/", "5", "]");
  performCalculation("1", "+", "(", "2", "+", "3", ")", "*", "3");
  performCalculation("2", "^", "3","^", "4");
  performCalculation("(", "2", "^", "3", ")", "^", "4");

}

    // See https://docs.oracle.com/javase/8/docs/technotes/guides/language/varargs.html
    public static void performCalculation (String ... valuesAndOperators)	{
	Vector aLine = new Vector();   //foreach��������ΪString���͵�vector
	StringBuilder test=new StringBuilder();
	 
	for ( String valuesAndOperator: valuesAndOperators )	{
		aLine.add(valuesAndOperator);
		test.append(valuesAndOperator);
		System.out.print(valuesAndOperator + " ");
		} 
	
	    String adjust=test.toString();
	    Vector<String> changeLine = new <String>Vector();
	    
	    boolean flag=false;
	    if(adjust.indexOf("(")!=-1) {
	    	flag=true;
	    }
	    boolean flag1=false;
	    if(adjust.indexOf("[")!=-1) {
	    	flag1=true;
	    }
	    boolean flag2=false;
	    if(adjust.indexOf("{")!=-1) {
	    	flag2=true;
	    }

	    
	    if(flag) {
		String s1=adjust.substring(adjust.indexOf("(")+1,adjust.indexOf(")"));
		Vector<String> changeLine1 = new <String>Vector();
		for(int i=0;i<s1.length();i++) changeLine1.add(s1.substring(i,i+1));
		int cur=(int)calculate(changeLine1);
		String ssn1;
		if (cur < 0) {
			ssn1 = "0" + cur;
		}
		else {
			ssn1 = "" + cur;
		}
		
		
        adjust = adjust.substring(0, adjust.indexOf('(')) + ssn1 + adjust.substring(adjust.indexOf(')')+1, adjust.length());
//        System.out.println(" ");
//        System.out.println(adjust);
	    }
		
	
		
		if(flag1) {
		String s2=adjust.substring(adjust.indexOf("[")+1,adjust.indexOf("]"));
//		String s3=s2.substring(s2.indexOf("[")+1,s2.indexOf("("));
//		String s4=s2.substring(s2.indexOf(")")+1,s2.indexOf("]"));
	      
		
		Vector<String> changeLine5 = new <String>Vector();
		
		for(int j=0;j<s2.length();j++) changeLine5.add(s2.substring(j,j+1));
		int ssn2=(int)calculate(changeLine5);

        adjust = adjust.substring(0, adjust.indexOf('[')) + ssn2 + adjust.substring(adjust.indexOf(']')+1, adjust.length());
//        System.out.println(" ");
//        System.out.println(adjust);
		
		}
		
		
		
		if(flag2) {
		String s6=adjust.substring(adjust.indexOf("{")+1,adjust.indexOf("}"));
//		String s7=s6.substring(s6.indexOf("{")+1,s6.indexOf("["));
//		String s8=s6.substring(s6.indexOf("]")+1,s6.indexOf("}"));
		//System.out.println(s6);
		Vector<String> changeLine9 = new <String>Vector();
		
		for(int k=0;k<s6.length();k++) changeLine9.add(s6.substring(k,k+1));
		int ssn=(int)calculate(changeLine9);
		
		adjust=adjust.substring(0,adjust.indexOf("{"))+ssn+adjust.substring(adjust.indexOf("}")+1,adjust.length());
				}
	
		
		for(int n=0;n<adjust.length();n++) changeLine.add(adjust.substring(n,n+1));
	    int initial=(int)calculate(changeLine);
	    System.out.println("="+initial);
	    
		
    }
    
    
    /** drives the calculation and returns the result
     */
    public static double calculate (Vector<String> inputLine) {
	while ( inputLine.size() >= 1 )	{//vector�е�inputLineû��Ԫ��ʱ����ѭ��
		if ( operator( inputLine.firstElement() )	) 
			performOperator(inputLine.firstElement());
		else
			performNumber(inputLine.firstElement());

		inputLine.removeElementAt(0);//ÿ�����۶�operator����number��������Ҫremoveһ��vector�е�һ��Ԫ��
	}
	while ( !  operatorStack.empty() )	{//operatorStack�в�Ϊ�գ�Ϊ��ʱ����ѭ��
		if ( numberStack.size() > 1 )//numberStack��ֻ�л���������
			evaluate();
		else	{
			System.out.println("dangling operand ....");
			System.out.println(numberStack);
			System.exit(1);
		
		}
	}
           
         
	return numberStack.pop();//������ս��
    }

    /** perform the required operation based on precedence of the operators on the stack
     */
    public static boolean operator (String op) {//opeartor���������ж�vector��һ��Ԫ����operators�ַ������Ƿ���ڣ�û�ҵ��򷵻�-1
	return ( operators.indexOf(op) >= 0 );
    }
    
    /**public static boolean bracket (String op) {//bracket���������ж�vector��һ��Ԫ����parentheses�ַ������Ƿ���ڣ�û�ҵ��򷵻�-1
    	return (parentheses.indexOf(op)>=0);
    } 
    */
    
    
    
    /** deteremence a precedence level for the operator
     */
    public static int precedence (String op) {
	return operators.indexOf(op);//��Ϊ�������±��������������ַ���String�Ҳ����������ȼ��ߣ�precedence����ֻ�𵽷����±������
    }

    /** perform the required operation based on precedence on the stack
     */
    public static void performOperator (String op) {
		while (! operatorStack.empty()  &&
			(  precedence(op) < precedence(operatorStack.peek() ) )//��2+3*5��������ʱ*�±�>+�±꣬���Բ�ִ��evaluate�������������push��operator��
		      )
				evaluate();
		operatorStack.push(op);
    }

    /** pushes the number on the number stack
     */
    public static void performNumber (String number) {
		numberStack.push(Double.valueOf(number));
    }

    /** get the number of the stack, if a number is available, else RIP
     */
    public static double  getNumber () {
	if ( numberStack.empty() ){
		System.out.println("not enough numbers ...");
		System.exit(2);
	} 
	return numberStack.pop();//��numberStack�е�Ԫ��pop��
    }

    /** perform the required ovperation based on the operator in the stack
     */
    public static void evaluate () {
		String currentOp = operatorStack.pop();//��operator�е�Ԫ��pop��
		double right = getNumber();
		double left = getNumber();
		if ( currentOp.equals("+") )
			numberStack.push( left + right );//�������Ľ���ٴ�����numberStack��
		else if ( currentOp.equals("-") )
			numberStack.push( left - right );
		else if ( currentOp.equals("*") )
			numberStack.push( left * right );
		else if ( currentOp.equals("%") )
			numberStack.push( left % right );
		else if ( currentOp.equals("/") )
			numberStack.push( left / right );
		else if ( currentOp.equals("^") )
			numberStack.push( Math.pow(left , right ) );
		else
			System.out.println("Unknow Operator");
    }
}