package lambda;

public class Abstraction extends Term {
	public Variable bound;
	public Term term;

	public Abstraction(Variable bound, Term term) {
		this.bound = bound;
		this.term = term;
	}

	@Override
	protected Term replace(Variable v, Term with) {
		return new Abstraction(bound, /* bound == v ? term : */ term.reduce().replace(v, with));
	}

	@Override
	public Term reduce() {
		Variable with = new Variable(bound.name, bound.n + 1);
		return new Abstraction(with, call(with));
	}

	@Override
	public Term call(Term arg) {
		return term.replace(bound, arg).reduce();
	}

	@Override
	public String toString() {
		return "\\" + bound + "." + term;
	}
}
