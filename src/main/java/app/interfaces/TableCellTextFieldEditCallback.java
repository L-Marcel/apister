package app.interfaces;

import app.core.HeaderEntry;

@FunctionalInterface
public interface TableCellTextFieldEditCallback {
    public void call(HeaderEntry entry, String current);
};
