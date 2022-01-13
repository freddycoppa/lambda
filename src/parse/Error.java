package parse;

public class Error {
	private String message;

	private static String repeat(int n) {
		String s = "";
		for (int i = 0; i < n; i++)
			s += ' ';
		return s;
	}

	public Error(String message, int pos) {
		this.message = repeat(pos + 1) + "^\n" + message;
	}

	@Override
	public String toString() {
		return this.message;
	}
}
