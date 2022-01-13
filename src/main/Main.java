package main;

import lex.*;
import parse.*;
import lambda.*;

import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Lexer lexer = new Lexer();
		Parser parser = new Parser(lexer.tokens());
		System.out.println("Lambda Interpreter Version 1.0");
		System.out.println(
				"Type a lambda expression at the prompt. Use '\\' (backslash) for lambda. Lambda terms cannot be assigned to variables. Type 'quit' to quit."
        );
		System.out.println("For example, 2^3 is written as (in Church encoding): (\\f.\\x.f(f(f x)))(\\f.\\x.f(f x))");
		System.out.println(
				"Note: Right-most expressions are given most preference. 'a b c' is interpreted as 'a(b c)', not '(a b)c'"
        );
		System.out.println(
				"Note: Lambda terms cannot be assigned to/saved in variables. '=' is interpreted as a variable's name"
        );
		System.out.print("in > ");
		String input = sc.nextLine();
		while (!input.equals("quit")) {
			lexer.reset(input);
			lexer.lex();
			parser.reset();
			Term f = null;
			try {
				f = parser.parse();
			} catch (Throwable e) {
				System.out.println("err? " + e);
			}
			if (parser.passed()) {
				if (f != null) {
					try {
						System.out.println("out> " + f);// .reduce());
					} catch (Throwable e) {
						System.out.println("err? " + e);
					}
				}
			} else {
				System.out.println("err> " + parser.error);
			}
			System.out.print("in > ");
			input = sc.nextLine();
		}
		sc.close();
	}
}
