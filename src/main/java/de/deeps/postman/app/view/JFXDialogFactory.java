package de.deeps.postman.app.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class JFXDialogFactory {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private StackPane stackPane;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private String title, content, dismiss;

    //convenience
    public JFXDialogFactory withTitle(String title){
        setTitle(title);
        return self();
    }

    public JFXDialogFactory withContent(String content){
        setContent(content);
        return self();
    }

    public JFXDialogFactory withDismiss(String dismiss){
        setDismiss(dismiss);
        return self();
    }

    public JFXDialogFactory withStackPane(StackPane stackPane){
        setStackPane(stackPane);
        return self();
    }

    public void show(){
        build().show();
    }

    //actions
    private JFXButton createDismissButton(JFXDialog dialog, String dismiss) {
        JFXButton dismissButton = new JFXButton(dismiss);
        dismissButton.setOnAction(event -> dialog.close());
        return dismissButton;
    }

    private JFXDialogLayout createLayout(String title, String content) {
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Text(title));
        layout.setBody(new Text(content));
        return layout;
    }

    private JFXDialog build(){
        JFXDialogLayout layout = createLayout(getTitle(), getContent());
        JFXDialog dialog = new JFXDialog(getStackPane(), layout, JFXDialog.DialogTransition.CENTER);
        JFXButton dismissButton = createDismissButton(dialog, getDismiss());
        layout.setActions(dismissButton);
        return dialog;
    }

    //accessing
    private JFXDialogFactory self(){
        return this;
    }
}
