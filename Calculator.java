
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
	Vector aLine = new Vector();   //foreach遍历泛型为String类型的vector
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
	while ( inputLine.size() >= 1 )	{//vector中的inputLine没有元素时跳出循环
		if ( operator( inputLine.firstElement() )	) 
			performOperator(inputLine.firstElement());
		else
			performNumber(inputLine.firstElement());

		inputLine.removeElementAt(0);//每次无论对operator还是number操作，都要remove一下vector中的一个元素
	}
	while ( !  operatorStack.empty() )	{//operatorStack中不为空，为空时跳出循环
		if ( numberStack.size() > 1 )//numberStack中只有还有数存在
			evaluate();
		else	{
			System.out.println("dangling operand ....");
			System.out.println(numberStack);
			System.exit(1);
		
		}
	}
           
         
	return numberStack.pop();//输出最终结果
    }

    /** perform the required operation based on precedence of the operators on the stack
     */
    public static boolean operator (String op) {//opeartor方法用来判断vector第一个元素在operators字符串中是否存在，没找到则返回-1
	return ( operators.indexOf(op) >= 0 );
    }
    
    /**public static boolean bracket (String op) {//bracket方法用来判断vector第一个元素在parentheses字符串中是否存在，没找到则返回-1
    	return (parentheses.indexOf(op)>=0);
    } 
    */
    
    
    
    /** deteremence a precedence level for the operator
     */
    public static int precedence (String op) {
	return operators.indexOf(op);//因为从左到右下标依次增大，所以字符串String右侧的运算符优先级高，precedence方法只起到返回下标的作用
    }

    /** perform the required operation based on precedence on the stack
     */
    public static void performOperator (String op) {
		while (! operatorStack.empty()  &&
			(  precedence(op) < precedence(operatorStack.peek() ) )//以2+3*5举例，此时*下标>+下标，所以不执行evaluate操作，将运算符push入operator中
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
	return numberStack.pop();//将numberStack中的元素pop出
    }

    /** perform the required ovperation based on the operator in the stack
     */
    public static void evaluate () {
		String currentOp = operatorStack.pop();//将operator中的元素pop出
		double right = getNumber();
		double left = getNumber();
		if ( currentOp.equals("+") )
			numberStack.push( left + right );//将计算后的结果再次推入numberStack中
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