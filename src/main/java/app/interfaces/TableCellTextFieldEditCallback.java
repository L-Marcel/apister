package app.interfaces;

@FunctionalInterface
public interface TableCellTextFieldEditCallback {
    public void call(int index, String current);
};
