package parse;

import lex.Token;
import lambda.*;
import java.util.*;

public class Parser {
	private List<Token> tokens;
	int index;

	public Parser(List<Token> tokens) {
		this.tokens = tokens;
		this.index = -1;
	}

	private Parser(List<Token> tokens, Map<String, Variable> variables, LinkedList<String> stack) {
		this.tokens = tokens;
		this.variables = variables;
		this.stack = stack;
		this.index = -1;
	}

	private Token nextToken() {
		Token token = null;
		if (hasNext()) {
			index++;
			token = tokens.get(index);
		}
		return token;
	}

	private boolean hasNext() {
		return index + 1 < tokens.size();
	}

	public Error error = null;

	public boolean passed() {
		return this.error == null;
	}

	private void raise(Error error) {
		this.error = error;
	}

	private Map<String, Variable> variables = new HashMap<String, Variable>();
	private LinkedList<String> stack = new LinkedList<String>();

	private void push(Variable f) {
		variables.put(f.toString(), f);
		stack.add(f.toString());
	}

	private void pop(int index) {
		Iterator<String> iter = stack.descendingIterator();
		int i = stack.size();
		int n = 0;
		while (iter.hasNext() && (i > index)) {
			variables.remove(iter.next());
			i--;
			n++;
		}
		for (int j = 0; j < n; j++)
			stack.removeLast();
	}

	private Term nextTerm() { // recursive descent parsing
		Term term = null;
		Token token = nextToken();
		if (token != null) {
			if (token.type == Token.Type.LAMBDA) {
				if (hasNext()) {
					token = nextToken();
					if (token.type == Token.Type.ID) {
						Variable v = new Variable(token.value);
						push(v);
						if (hasNext()) {
							token = nextToken();
							if (token.type == Token.Type.DOT) {
								Term t = nextTerm();
								if (t != null)
									/* if (passed()) */ term = new Abstraction(v, t);
								else
									raise(new Error("Expected a term!", token.pos));
							} else
								raise(new Error("Expected a dot!", token.pos));
						} else
							raise(new Error("Expected a dot!", token.pos));
					} else
						raise(new Error("Expected a variable!", token.pos));
				} else
					raise(new Error("Expected a variable!", token.pos));
			} else if (token.type == Token.Type.ID) {
				if (!variables.containsKey(token.value)) {
					// variables.put(token.value, new Variable(token.value));
					push(new Variable(token.value));
				}
				term = variables.get(token.value);
				if (hasNext()) {
					int index = stack.size();
					Term arg = nextTerm();
					if (passed()) {
						// term = new Application(term, arg);
						term = term.call(arg);
						pop(index);
					}
				}
			} else if (token.type == Token.Type.OPEN_PAREN) {
				List<Token> subTokens = new LinkedList<Token>();
				int parens = 1;
				while (parens != 0 && hasNext()) {
					token = nextToken();
					if (token.type == Token.Type.OPEN_PAREN)
						parens++;
					else if (token.type == Token.Type.CLOSE_PAREN)
						parens--;
					subTokens.add(token);
				}
				if (parens != 0)
					raise(new Error("Expected ')'!", token.pos));
				else {
					subTokens.remove(subTokens.size() - 1); // removing the ')' token from the end of the list
					Parser subParser = new Parser(subTokens, variables, stack); // the subParser inherits all of the
																				// variables from this parser
					term = subParser.nextTerm();
					raise(subParser.error); // if subParser.error is null, this doesn't make a difference
					if (passed()) {
						if (hasNext()) {
							int index = stack.size();
							Term arg = nextTerm();
							if (passed()) {
								// term = new Application(term, arg);
								term = term.call(arg);
								pop(index);
							}
						}
					}
				}
			} else
				raise(new Error("Expected a term!", token.pos));
		}
		return term;
	}

	public Term parse() {
		return nextTerm();
	}

	public void reset() {
		this.error = null;
		variables.clear();
		stack.clear();
		index = -1;
	}
}
