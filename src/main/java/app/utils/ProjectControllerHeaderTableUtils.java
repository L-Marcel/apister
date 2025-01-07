package app.utils;

import java.util.Comparator;

import app.core.HeaderEntry;
import app.layout.TableCellTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ProjectControllerHeaderTableUtils {
    //#region Config
    public static void configHeaderTable(
        TableView<HeaderEntry> headerTableView,
        TableColumn<HeaderEntry, String> keysTableColumn,
        TableColumn<HeaderEntry, String> valuesTableColumn
    ) {
        keysTableColumn.setCellValueFactory(cell -> cell.getValue().keyProperty());
        keysTableColumn.setCellFactory(column -> new TableCellTextField(
            (entry, current) -> {
                entry.setKey(current);
                ProjectControllerHeaderTableUtils.cleanupAndAddEmptyRowIfNeeded(
                    headerTableView.getItems()
                );
            }
        ));

        valuesTableColumn.setCellValueFactory(cell -> cell.getValue().valueProperty());
        valuesTableColumn.setCellFactory(column -> new TableCellTextField(
            (entry, current) -> {
                entry.setValue(current);
                ProjectControllerHeaderTableUtils.cleanupAndAddEmptyRowIfNeeded(
                    headerTableView.getItems()
                );
            }
        ));

        headerTableView.sortPolicyProperty().set(table -> {
            FXCollections.sort(table.getItems(), (a, b) -> {
                boolean aIsBlank = a.getKey().isBlank() && a.getValue().isBlank();
                boolean bIsBlank = b.getKey().isBlank() && b.getValue().isBlank();

                if(aIsBlank && !bIsBlank) return 1;
                if(!bIsBlank && aIsBlank) return -1;

                Comparator<HeaderEntry> comparator = table.getComparator();
                return comparator == null ? 0 : comparator.compare(a, b);
            });

            return true;
        });
    };
    //#endregion
    //#region Flow
    public static void cleanupAndAddEmptyRowIfNeeded(ObservableList<HeaderEntry> rows) {
        if(rows.size() <= 0) {
            rows.add(new HeaderEntry());
        } else {
            HeaderEntry last = rows.get(rows.size() - 1);
            if(!last.getKey().isBlank() && !last.getValue().isBlank()) {
                rows.add(new HeaderEntry());
            };

            cleanEmptyRows(rows);
        };
    };

    private static void cleanEmptyRows(ObservableList<HeaderEntry> rows) {
        for(int i = 0; i < rows.size(); i++) {
            HeaderEntry entry = rows.get(i);
            if(
                entry.getKey().isBlank() && 
                entry.getValue().isBlank() && 
                i != rows.size() - 1
            ) {
                rows.remove(i);
                i--;
            };
        };
    };
    //#endregion
};
