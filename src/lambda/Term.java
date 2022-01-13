package lambda;

public abstract class Term {
	protected abstract Term replace(Variable v, Term with); // alpha conversion

	public abstract Term reduce(); // beta reduction

	public Term call(Term arg) {
		return new Application(this, arg.reduce());
	}
}
