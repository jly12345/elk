package com.jxx.demo;

import com.jxx.demo.service.WorkService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
        log.info("project starting...");
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color:#FFF5EE");

        Label srcLable = new Label("源excel完整路径:");
        int fontSize = 14;
        srcLable.setFont(Font.font(fontSize));
        Label targeLabel = new Label("目标excel完整路径:");
        targeLabel.setFont(Font.font(fontSize));

        final TextField src_text = new TextField("D:\\logs\\202001考期中开新系统成绩文件(学生用）.xlsx");
        final TextField target_text = new TextField("D:\\logs\\课程报考表及成绩册(19秋行政管理本科).xls");
        final TextField master_text = new TextField("蒋志军");
        Label masterLabel = new Label("班主任:");

        Button clearBtn = new Button("重置");
        Button sureBtn = new Button("确定");

        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(srcLable, 0, 0);
        gridPane.add(src_text, 1, 0);

        gridPane.add(targeLabel, 0, 1);
        gridPane.add(target_text, 1, 1);

        gridPane.add(masterLabel, 0, 2);
        gridPane.add(master_text, 1, 2);

        gridPane.add(sureBtn, 0, 3);
        gridPane.add(clearBtn, 1, 3);

        gridPane.setHgap(5);
        gridPane.setVgap(15);

        GridPane.setMargin(clearBtn, new Insets(0, 0, 0, 30));
        ProgressBarContext.getInstance().setVisible(false);
        gridPane.add(ProgressBarContext.getInstance(), 0, 5);

        Scene scene = new Scene(gridPane);

        primaryStage.setTitle("excel小工具");
        primaryStage.setScene(scene);
        primaryStage.setWidth(500);
        primaryStage.setHeight(300);
        primaryStage.setResizable(false);
        primaryStage.show();

        clearBtn.setOnAction(event -> {
            src_text.clear();
            target_text.clear();
        });

        sureBtn.setOnAction(event -> {
            String srcPath = src_text.getText();
            String targetPath = target_text.getText();
            String masterName = master_text.getText();
            WorkService controller = new WorkService(srcPath, targetPath, masterName);
            ProgressBarContext.getInstance().setVisible(true);
            ProgressBarContext.getInstance().setProgress(0);
            Map<String, String> map = controller.startWork();

            if(map != null) {
                String msg = map.get("msg");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.titleProperty().set("信息");
                alert.headerTextProperty().set(msg);
                alert.showAndWait();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
