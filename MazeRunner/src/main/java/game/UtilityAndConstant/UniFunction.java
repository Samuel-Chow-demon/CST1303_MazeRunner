package game.UtilityAndConstant;

@FunctionalInterface
public interface UniFunction<T, R> {
    R apply(T t);
}