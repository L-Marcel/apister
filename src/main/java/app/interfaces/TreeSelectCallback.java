package app.interfaces;

import app.core.Request;

@FunctionalInterface
public interface TreeSelectCallback {
    public void call(Request request);
};
