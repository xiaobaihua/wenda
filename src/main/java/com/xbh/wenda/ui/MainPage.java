package com.xbh.wenda.ui;

import com.xbh.wenda.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @author xbh
 * @date 2019年7月28日15:34:29
 * @Description 主控制器
 */
public class MainPage extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		final URL fxmlUrl = Main.class.getResource("mainPage.fxml");
//		ui/mainPage.fxml
		AnchorPane root = FXMLLoader.load(fxmlUrl);

		final Scene scene = new Scene(root);
		primaryStage.setScene(scene);

		primaryStage.show();
	}
}