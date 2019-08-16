package com.xbh.wenda.ui;

import com.xbh.wenda.Main;
import com.xbh.wenda.bean.WdBean;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
/**
 * @author xbh
 * @date 2019年7月28日15:34:29
 * @Description 主控制器
 */
public class MainPage extends Application {

	@FXML
	private Label inputExcel;

	@FXML
	private Label outputExcel;

	@FXML
	public Label total;



	@FXML
	public void selectInputExcelFile() {
		final File file = this.openFile();

		inputExcel.setText(file.getPath());
		outputExcel.setText(file.getPath());
		Main.setInputName(file.getPath());

//		stage.show();
	}

	private File openFile() {
		final Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("请选择输入EXCEL");
		final File file = fileChooser.showOpenDialog(stage);

		if (file.length() > 0) {
			return file;
		}
		throw new RuntimeException("数据获取异常");
	}

//	public static void main(String[] args) {
//		launch(args);
//	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		final URL fxmlUrl = getClass().getResource("page/mainPage.fxml");
		AnchorPane root = FXMLLoader.load(fxmlUrl);
		final Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setOnCloseRequest((event)->{
			System.exit(0);
		});
	}

	public void selectOutputExcel(MouseEvent mouseEvent) {
		final File file = this.openFile();

		Main.setOutputName(file.getPath());
	}

	public static void execute() {
		if (Main.getBeanList() == null) {
			Main.setBeanList(new ArrayList<WdBean>());
		}

		try {
			Main.readExcel(Main.getInputName(), Main.getBeanList());

			// 获取百度结果页
			Main.getBaiduResultUrl(Main.getBeanList());

			// 获取问答页面url
			Main.getQAndAUrl(Main.getBeanList());

			// 获取最终页面
			Main.getQAndAPage(Main.getBeanList());

			Main.write(Main.getOutputName(), Main.getBeanList());

			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void run(MouseEvent mouseEvent) throws IOException {
		new Thread(()->{
			MainPage.execute();
		}).start();

		final long time = System.currentTimeMillis();

		test();
//		while (true) {
//			try {
//				final Timeline timeline = new Timeline();
//				timeline.stop();
//
//				timeline.play();
//				total.setText(String.valueOf(System.currentTimeMillis()));
//				Thread.currentThread().sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}

	private void test() {
		Service<String> service=new Service<String>() {
			@Override
			protected Task<String> createTask() {
				return new Task<String>() {
					@Override
					protected String call() throws Exception {

						while (true){
							//更新service的value属性
							updateValue("最大"+Main.getBeanList().size() * 20 * 2+ "| 已完成:" + Main.getI());
//							//更新service的progress属性
//							updateProgress(a, 100d);
							Thread.sleep(100);
						}
					}
				};
			}
		};

		this.total.textProperty().bind(service.valueProperty());

		service.start();
	}



}