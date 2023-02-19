package veijalainen.eljas.tiralabra.util;

public final class Pair<LT, RT> {
	public final LT left;
	public final RT right;

	public Pair(LT left, RT right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Pair<?, ?> pair = (Pair<?, ?>) o;
		if (!left.equals(pair.left)) {
			return false;
		}
		return right.equals(pair.right);
	}

	@Override
	public int hashCode() {
		int result = left.hashCode();
		result = 31 * result + right.hashCode();
		return result;
	}
}
