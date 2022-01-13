package lambda;

public class Variable extends Term {
	String name;
	int n; // used to avoid name clashing with other variables. For example, \x.\x.T might get renamed to \x1.\x2.T

	public Variable(String name) {
		this(name, -1);
	}

	Variable(String name, int n) {
		this.name = name;
		this.n = n;
	}

	@Override
	protected Term replace(Variable v, Term with) {
		return this == v ? with : this;
	}

	@Override
	public Term reduce() {
		return this;
	}

	@Override
	public String toString() {
		return n == -1 || n == 0 ? name : name + n;
	}
}
