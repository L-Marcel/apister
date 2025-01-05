package app.interfaces;

import app.errors.InvalidInput;

@FunctionalInterface
public interface Validator<R> {
    public abstract void validate(R candidate) throws InvalidInput;
};
