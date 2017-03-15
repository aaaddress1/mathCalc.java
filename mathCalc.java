/**
 * @file mathCalc.java
 * @author Sheng-Hao Ma (aaaddress1, aka adr)
 * @date 2017/03/15
 *
 * @brief compute the result of any type of mathematical formulas
**/

import java.util.*;
public class mathCalc {

	public static boolean isOperator(char ch) {
		return 	ch == '+' || ch == '-' || ch == '*' || ch == '/';
	}

	public static int level(char ch) {
		switch (ch) {
			case '(': case ')': return 1;
			case '+': case '-': return 2;
			case '*': case '/': case '%': return 3;
			default: return ch >= '0' && ch <= '9'? /* normal number */ 0 : /* worst case */-1;
		}
	}

	public static int compute(int x, int y, char op) {
		switch (op) {
			case '+': return x + y;
			case '-': return y - x;
			case '*': return x * y;
			case '/': return y / x;
			default: return -1; // worst case
		}
	}
	public static int calc(String mathicFmul) {

		if (mathicFmul.length() < 1)
			return 0;

		String buff = "";
		Stack<Integer> valueStack = new Stack<Integer>();
		Stack<Character> operatorStack = new Stack<Character>();

		for (char ch: mathicFmul.toCharArray()) {

			/*
				left parenthesis, push into operator stack for preparing
			*/
			if (ch == '(')
				operatorStack.push(ch);

			/*
				right parenthesis, pop every operator with two numbers to compute the result,
				and push result into value stack
			*/
			else if (ch == ')') {
				if (buff != "") valueStack.push(Integer.parseInt(buff));
				buff = "";
				while (operatorStack.peek() != '(')
					valueStack.push(compute(valueStack.pop(), valueStack.pop(), operatorStack.pop()));
				operatorStack.pop();
			}

			/*
				push operator into stack, except meeting higher-priority operator at top of the stack.
				if higher-priority operator at top of the stack, first pop it with two numbers and compute the result,
				and then push the result into value stack until the priority of current operator is equal or higher
				than top of operator stack
			*/
			else if (isOperator(ch)) {
				if (buff != "") valueStack.push(Integer.parseInt(buff));
				buff = "";

				if (operatorStack.isEmpty())
					operatorStack.push(ch);

				else if (level(ch) > level(operatorStack.peek()))
					operatorStack.push(ch);

				else {
					while (!operatorStack.isEmpty() && level(ch) <= level(operatorStack.peek()))
						valueStack.push(compute(valueStack.pop(), valueStack.pop(), operatorStack.pop()));
					operatorStack.push(ch);
				}

			}
			else buff += ch;
		}

		/*
			if there's any operator in the stack, we should pop everyone with two value to compute the result,
			and push the result into the value stack, ...until no operator in the stack and only one number
			in the value stack.
		*/
		while (!operatorStack.isEmpty()) {
			if (buff != "") valueStack.push(Integer.parseInt(buff));
			buff = "";
			valueStack.push(compute(valueStack.pop(), valueStack.pop(), operatorStack.pop()));
		}

		return valueStack.pop();
	}
	public static void main(String[] args) {

		System.out.println("welcome to magic math calculator!");
		System.out.println("@author: aaaddress1@gmail.com");
		System.out.println("");
		while (true) {
			System.out.print("> ");
			String mathFormulas = System.console().readLine();

			if (mathFormulas.length() > 0) System.out.println(calc(mathFormulas));
		}
	}
}
