package node;

import connectpoint.ChildConnectPoint;
import connectpoint.ParentConnectPoint;
import connectpoint.TipsConnectPoint;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import node.nodedata.BaseNodeData;
import node.nodedata.TipsNodeData;

import java.util.ArrayList;

public class BaseNode {
    //text area
    protected TextArea mainTextArea;
    protected Text onlyReadText;
    protected Button deleteButton;
    protected Button drawButton;
    protected ColorPicker colorPicker;
    protected BooleanProperty colorPickerVisible;

    protected ChildConnectPoint childConnectPoint;
    protected ParentConnectPoint parentConnectPoint;
    protected ArrayList<TipsConnectPoint> tipsConnectPoints;

    public BaseNode() {
        mainTextArea = new TextArea();
        mainTextArea.setVisible(false);
        onlyReadText = new Text();
        deleteButton = new Button("x");
        deleteButton.setVisible(false);
        drawButton = new Button();
        drawButton.setVisible(false);
        colorPicker = new ColorPicker();
        colorPickerVisible = new SimpleBooleanProperty(false);
        colorPicker.visibleProperty().bind(colorPickerVisible);
    }

    //operate
    protected void bindContent(Pane mainArea) {}

    //about content
    protected void addMainTextArea(Shape shape) {
        shape.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                mainTextArea.setVisible(true);
            } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                if (colorPickerVisible.get()) {
                    colorPickerVisible.set(false);
                    System.out.println("颜色选择框已禁用");
                }
                else {
                    colorPickerVisible.set(true);
                    System.out.println("颜色选择框已启用");
                }

            }
        });
        shape.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> mainTextArea.setVisible(false));
        mainTextArea.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> mainTextArea.setVisible(true));
        mainTextArea.setWrapText(true);
        mainTextArea.setFont(Font.font(null, FontWeight.MEDIUM, 14));
        mainTextArea.textProperty().addListener((observableValue, s, t1) -> {
            getNodeData().updateChangeDate();
            if (getNodeData().getChildNodeData() != null) {
                for (BaseNodeData k: getNodeData().getChildNodeData()
                ) {
                    if (k instanceof TipsNodeData) {
                        ((TipsNode) k.getNode()).updateText();
                    }
                }
            }
        });
        onlyReadText.setFont(Font.font(null, FontWeight.MEDIUM , 14));
        onlyReadText.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                mainTextArea.setVisible(true);
            } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                setClipboardText(onlyReadText);
                System.out.println("已复制框中内容");
            }
        });
    }

    protected void setClipboardText(Text text) {
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(text.getText());
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }

    protected void addDeleteButton(Shape shape) {
        shape.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> deleteButton.setVisible(true));
        shape.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> deleteButton.setVisible(false));
        deleteButton.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> deleteButton.setVisible(true));
        deleteButton.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> deleteButton.setVisible(false));
        onlyReadText.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> deleteButton.setVisible(true));
        onlyReadText.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> deleteButton.setVisible(false));
    }

    protected void setColorPicker() {}

    //about points
    public ArrayList<TipsConnectPoint> getTipsConnectPoints() {
        return tipsConnectPoints;
    }

    public ChildConnectPoint getChildConnectPoint() {
        return childConnectPoint;
    }

    public ParentConnectPoint getParentConnectPoint() {
        return parentConnectPoint;
    }

    //about node
    public BaseNodeData getNodeData() {
        return null;
    }

    //about position
    public void setXProperty(double startX) {

    }

    public void setYProperty(double startY) {}

    public void setWidthProperty(double width) {}

    public void setHeightProperty(double height) {}
}
