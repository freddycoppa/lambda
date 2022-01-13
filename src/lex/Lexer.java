package lex;

import java.util.*;

public class Lexer {
	private String expr;
	private int index;
	private List<Token> tokens = new ArrayList<Token>();

	public void reset(String expr) {
		this.expr = expr;
		this.index = -1;
		this.tokens.clear();
	}

	private boolean advance() {
		boolean flag = this.hasNext();
		index++;
		return flag;
	}

	public boolean hasNext() {
		return index + 1 < expr.length();
	}

	private String makeID() {
		String s = "";
		do {
			s += this.expr.charAt(index);
		} while (this.advance() && !" ()\\.".contains("" + this.expr.charAt(index)));
		this.index--;
		return s;
	}

	private void skipSpace() {
		char c;
		do {
			c = this.expr.charAt(index);
		} while (this.advance() && (c == ' '));
		this.index -= 2;
	}

	private Token nextToken() {
		Token token = null;
		if (this.advance())
			switch (expr.charAt(index)) {
			case ' ':
				if (this.hasNext()) {
					this.skipSpace();
					token = this.nextToken();
				}
				break;
			case '(':
				token = new Token(Token.Type.OPEN_PAREN, index);
				break;
			case ')':
				token = new Token(Token.Type.CLOSE_PAREN, index);
				break;
			case '\\':
				token = new Token(Token.Type.LAMBDA, index);
				break;
			case '.':
				token = new Token(Token.Type.DOT, index);
				break;
			default:
				token = new Token(Token.Type.ID, this.makeID(), index);
			}
		return token;
	}

	public void lex() {
		Token token = nextToken();
		while (token != null) {
			tokens.add(token);
			token = nextToken();
		}
	}

	public List<Token> tokens() {
		return this.tokens;
	}
}
