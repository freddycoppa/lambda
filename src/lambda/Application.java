package lambda;

public class Application extends Term {
	public Term func, arg;

	public Application(Term func, Term arg) {
		this.func = func;
		this.arg = arg;
	}

	@Override
	protected Term replace(Variable v, Term with) {
		return new Application(func.replace(v, with), arg.replace(v, with));
	}

	@Override
	public Term reduce() {
		return func.reduce().call(arg);
	}

	@Override
	public String toString() {
		return "(" + func + ")" + arg;
	}
}
