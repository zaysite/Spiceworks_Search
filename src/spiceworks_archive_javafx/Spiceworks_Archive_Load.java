package spiceworks_archive_javafx;

/*
 * The MIT License
 *
 * Copyright 2016 it.student.
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

import static com.sun.deploy.util.ReflectionUtil.instanceOf;
import javafx.application.Preloader;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import static com.sun.deploy.util.ReflectionUtil.instanceOf;
import javafx.scene.control.Label;
import static com.sun.deploy.util.ReflectionUtil.instanceOf;
import static com.sun.deploy.util.ReflectionUtil.instanceOf;
import static com.sun.deploy.util.ReflectionUtil.instanceOf;
import static com.sun.deploy.util.ReflectionUtil.instanceOf;
import static com.sun.deploy.util.ReflectionUtil.instanceOf;
import static com.sun.deploy.util.ReflectionUtil.instanceOf;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author it.student
 */
public class Spiceworks_Archive_Load extends Preloader
{
    
    private ProgressBar progress_Bar;
    private Label label;
    private Stage stage;
    private final int HEIGHT = 150;
    private final int WIDTH = 300;
    
    private Scene createPreloaderScene()
    {
        label = new Label("...LOADING...");
        progress_Bar = new ProgressBar(0.0);
        progress_Bar.setProgress(0.0);
        
        BorderPane border_Pane = new BorderPane();
        VBox Hbox = new VBox();
        Hbox.setAlignment(Pos.CENTER);
        Hbox.getChildren().addAll(label,progress_Bar);
        border_Pane.setCenter(Hbox);
        
        return new Scene(border_Pane, WIDTH, HEIGHT);        
    }
    
    @Override
    public void start(Stage stage) throws Exception
    {
        this.stage = stage;
        stage.setScene(createPreloaderScene());        
        
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
            label.setText(pn.getLabel());
        }
    }
    
   
    
}
