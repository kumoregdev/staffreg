package org.kumoricon.staff.client;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import org.kumoricon.staff.client.model.Staff;

import javax.annotation.PostConstruct;

public class ViewModel {

    private final ObjectProperty<Node> mainView = new SimpleObjectProperty(this, "mainView", null);
    private final BooleanProperty preferencesMenuDisabled = new SimpleBooleanProperty(this, "preferencesMenuDisabled", true);
    private final BooleanProperty refreshMenuDisabled = new SimpleBooleanProperty(this, "refreshMenuDisabled", true);

    private final ObjectProperty<Staff> selectedStaff = new SimpleObjectProperty<>(this, "selectedStaff", null);

    public ObjectProperty<Node> mainViewProperty() {
        return mainView;
    }

    public final Node getMainView() {
        return mainView.get();
    }

    public final void setMainView(Node mainView) {
        this.mainView.set(mainView);
    }

    public final void disablePreferencesMenu(boolean disabled) {
        preferencesMenuDisabled.set(disabled);
    }

    public final BooleanProperty preferencesMenuDisabledProperty() {
        return preferencesMenuDisabled;
    }


    public final void disableRefreshMenu(boolean disabled) {
        refreshMenuDisabled.set(disabled);
    }
    public final BooleanProperty refreshMenuDisabledProperty() {
        return refreshMenuDisabled;
    }

    public final Staff getSelectedStaff() {
        return selectedStaff.get();
    }

    public final void setSelectedStaff(Staff staff) {
        selectedStaff.setValue(staff);
    }

}