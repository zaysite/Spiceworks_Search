package spiceworks_archive_javafx;

/*
 * The MIT License
 *
 * Copyright 2016 Jordan Kahtava.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import javafx.application.Preloader;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author it.student
 */
public class Spiceworks_Archive_Load extends Preloader
{
    
    private ProgressBar progress_Bar;
    private Label title_Label;
    private Label progress_Label;
    private Stage stage;
    private final int HEIGHT = 350;
    private final int WIDTH = 520;
    
    /**
     * 
     * @return 
     */
    private Scene createPreloaderScene()
    {
        title_Label = new Label("Welcome to Jordans Spicework Ticket Search");
        title_Label.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
        
        progress_Label = new Label("...LOADING...");
        progress_Label.setStyle("-fx-text-fill: white;");
        progress_Bar = new ProgressBar(0.0);
        progress_Bar.setProgress(0.0);
        
        BorderPane border_Pane = new BorderPane();
        VBox VBox_Title = new VBox();
        VBox_Title.setAlignment(Pos.CENTER);
        VBox_Title.setStyle("-fx-background-color: black;");
        VBox_Title.getChildren().addAll(title_Label);
        
        Pane pane = new Pane();
        pane.setStyle("-fx-background-image: url(\"spiceworks_archive_javafx/Images/coffee-777612_1280.jpg\"); -fx-background-size: 520,350; -fx-background-repeat: no-repeat");
        
        VBox VBox = new VBox();
        VBox.setAlignment(Pos.CENTER);
        VBox.setStyle("-fx-background-color: black;");
        VBox.getChildren().addAll(progress_Label,progress_Bar);
        //VBox.setStyle("-fx-background-image: url(\"spiceworks_archive_javafx/Images/coffee-777612_1280.jpg\"); -fx-background-size: 520,350; -fx-background-repeat: no-repeat");
        //-fx-background-size: 300,150;
        
        //border_Pane.setStyle("-fx-background-image: url(\"spiceworks_archive_javafx/Images/coffee-777612_1280.jpg\"); -fx-background-size: 520,350; -fx-background-repeat: no-repeat; ");
        border_Pane.setTop(VBox_Title);
        border_Pane.setCenter(pane);
        border_Pane.setBottom(VBox);
        
        
        return new Scene(border_Pane, WIDTH, HEIGHT);        
    }
    
    @Override
    public void start(Stage stage) throws Exception
    {
        this.stage = stage;
        stage.setScene(createPreloaderScene());     
        stage.setMaxHeight(HEIGHT+20);
        stage.setMaxWidth(WIDTH);
        stage.setMinHeight(HEIGHT+20);
        stage.setMinWidth(WIDTH);
        stage.setTitle("Initializing Search Program");
        Image icon64 = new Image("spiceworks_archive_javafx/Images/64x64.png");
        Image icon32 = new Image("spiceworks_archive_javafx/Images/32x32.png");
        Image icon16 = new Image("spiceworks_archive_javafx/Images/16x16.png");
        stage.getIcons().addAll(icon64,icon32,icon16);
        //stage.setResizable(false);
        stage.show();
    }
    
    @Override
    public void handleStateChangeNotification(StateChangeNotification scn)
    {
        if (scn.getType() == StateChangeNotification.Type.BEFORE_START)
        {
            stage.hide();
        }
    }
    
    @Override
    public void handleProgressNotification(ProgressNotification pn)
    {
        progress_Bar.setProgress(pn.getProgress());
    }    
    
    @Override
    public void handleApplicationNotification(PreloaderNotification preloader_Notification)
    {
        if(preloader_Notification instanceof ProgressNotification)
        {
            ProgressNotification pn= (ProgressNotification) preloader_Notification;
            progress_Bar.setProgress(pn.getProgress());
        }
        if(preloader_Notification instanceof LabelNotification)
        {
            LabelNotification pn= (LabelNotification) preloader_Notification;
            progress_Label.setText(pn.getLabel());
        }
    }
    
   
    
}
