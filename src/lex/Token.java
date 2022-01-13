package lex;

public class Token {
	public static enum Type {
		OPEN_PAREN, CLOSE_PAREN, ID, LAMBDA, DOT
	}

	public Type type;
	public String value;
	public int pos;

	public Token() {}

	public Token(Type type, int pos) {
		this.type = type;
		this.pos = pos;
	}

	public Token(Type type, String value, int pos) {
		this.type = type;
		this.value = value;
		this.pos = pos;
	}

	@Override
	public String toString() {
		return value != null ? type.name() + " : " + value : type.name();
	}
}
