package app.interfaces;

import app.core.Request;

@FunctionalInterface
public interface TreeUnselectCallback {
    public void call(Request request);
};
