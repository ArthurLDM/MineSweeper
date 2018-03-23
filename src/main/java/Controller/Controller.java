package Controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import Enums.BoardSize;
import Enums.CellStatus;
import Enums.MouseAction;
import GameControl.Board;
import GameControl.Cell;
import GameControl.Game;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class Controller
{

    //difficulty selection
    @FXML
    private ToggleGroup selectedDifficulty;
    @FXML
    private RadioButton beginner;
    @FXML
    private RadioButton intermediate;
    @FXML
    private RadioButton expert;
    @FXML
    private RadioButton customized;
    @FXML
    private TextField columnsTextField;
    @FXML
    private TextField rowsTextField;
    @FXML
    private TextField minesTextField;
    @FXML
    private Button startButton;
    @FXML
    private Button newGameButton;
    @FXML
    private Button restartButton;

    //displaying message
    @FXML
    private Label message;

    // displays timer(=score)
    @FXML
    Label timerLabel;

    //grid
    @FXML
    private Pane gridContainer;
    @FXML
    private GridPane gridpane;



    private Game game;

    private void createGame()
    {
        BoardSize difficulty = getDifficulty();
        Board board = new Board(difficulty);
        game = new Game(board);
    }



    private Timer timer=new Timer();
    //do something on timer tick
    class displayAndUpdateScore extends TimerTask
    {

        private int i = 0;
        public void run()
        {
            if (!game.isHasEnded())
            {
                //update score
                game.setScore(++i);

                //runLater to avoid threading problems
                Platform.runLater(() -> {
                    //display score
                    timerLabel.setText("Score : " + String.valueOf(game.getScore()));
                });
            }
        }
    }


    ///////////GAME CONTROLLERS///////////
    @FXML
    private void startGame()
    {
        createGame();

        gridSet();
        //creates the grid
        setSizeGridPane();
        //fills the grid with buttons (one for each cell)
        createButtons();


        message.setText("Game Started");
        startButton.setDisable(true);
        newGameButton.setDisable(false);
        restartButton.setDisable(false);

        //timer used for the score
        setTimer();


    }

    @FXML
    private void restartGame()
    {
        gridSet();

        timer.purge();
        timer.cancel();

        Game gameKeeper = this.game;
        startGame();
        this.game=gameKeeper;
        this.game.restartGame();
    }

    @FXML
    private void newGame()
    {
        gridSet();

        timer.purge();
        timer.cancel();
        startGame();
    }
    //////////////////////////////////////



    ///////////USEFUL METHODS///////////
    //method transforming and index into a position on th grid
    private Point getPositionFromName(int index)
    {
        BoardSize boardSize = this.game.getBoard().getBoardSize();
        int height = boardSize.getHeight();
        int length = boardSize.getLength();

        int x = index % length;
        int y = index / height;
        return new Point(x,y);
    }

    private Image getImage(String path)
    {
        return new Image(path);
    }


    private void setTimer()
    {
        timer = new Timer();
        TimerTask task = new displayAndUpdateScore();
        timer.schedule(task, 0, 1000);
    }


    //TODO Add to CSS
    private Font loadFont()
    {

        return  Font.loadFont(Objects.requireNonNull(getClass().getClassLoader().getResource
                ("fonts/minesweeper.ttf")).toExternalForm(), 30);
    }

    private Font font = loadFont();
    /////////////////////////////////////



    ///////////GETTING OR CREATING DIFFICULTY///////////
    @FXML
    private void customizedNotSelected()
    {
        //empty textField
        columnsTextField.setText("");
        rowsTextField.setText("");
        minesTextField.setText("");

        //disable textField
        columnsTextField.setEditable(false);
        rowsTextField.setEditable(false);
        minesTextField.setEditable(false);
    }

    @FXML
    private void selectCustomized()
    {
        if(customized.isSelected())
        {
            columnsTextField.setEditable(true);
            rowsTextField.setEditable(true);
            minesTextField.setEditable(true);
        }

    }

    private BoardSize getDifficulty()
    {
        RadioButton selectedRadioButton = (RadioButton)selectedDifficulty.getSelectedToggle();

        if (selectedRadioButton == beginner)
            return BoardSize.BEGINNER;
        else if (selectedRadioButton == intermediate)
            return BoardSize.INTERMEDIATE;
        else if (selectedRadioButton == expert)
            return BoardSize.EXPERT;
        else
        {
            int rows = Integer.parseInt(rowsTextField.getText());
            int columns = Integer.parseInt(columnsTextField.getText());
            int mines = Integer.parseInt(minesTextField.getText());

            if (rows<30 && columns<30 && mines>1 && mines<(rows*columns-1))
            {
                BoardSize.CUSTOMIZED.setHeight(rows);
                BoardSize.CUSTOMIZED.setLength(columns);
                BoardSize.CUSTOMIZED.setNumMines(mines);
            }
            return BoardSize.CUSTOMIZED;
        }

    }

    ////////////////////////////////////////////////////



    ///////////GRID CREATION AND FILLING///////////

    private void gridSet()
    {
        //delete actual grid
        gridContainer.getChildren().clear();

        //create and add a new one
        gridpane = new GridPane();
        //the grid occupies as much space as possible
        gridpane.setPrefSize(gridContainer.getWidth(),gridContainer.getHeight());
        gridContainer.getChildren().add(gridpane);

    }

    private void setSizeGridPane()
    {
        BoardSize difficulty = getDifficulty();

        int width = difficulty.getHeight();
        int length = difficulty.getLength();

        //each cell is of equal size and all the cell occupy as much space as possible
        for (int column = 0; column < length; column++)
        {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100/length);
            gridpane.getColumnConstraints().add(columnConstraints);
        }

        for (int row = 0; row < width; row++)
        {
            RowConstraints rowConstraint = new RowConstraints();
            rowConstraint.setPercentHeight(100/width);
            gridpane.getRowConstraints().add(rowConstraint);
        }
    }

    private void createButtons()
    {

        BoardSize difficulty = getDifficulty();

        int width = difficulty.getHeight();
        int length = difficulty.getLength();


        for (int line = 0; line < width; line++)
        {
            for (int row = 0; row < length; row++)
            {
                int num = width * line + row;
                Button button = createButton(num);
                gridpane.add(button, row, line);
            }
        }
    }



    private Button createButton(int number)
    {
        final Button button = new Button();

        //button style
        //TODO Export to CSS file
        button.setId(String.valueOf(number));
        button.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        button.setStyle("-fx-focus-color: transparent;");

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        button.setEffect(dropShadow);


        button.setStyle("-fx-padding: 0.333333em 0.666667em 0.333333em 0.666667em;"); /* 4 8 4 8 */
        button.setStyle("-fx-alignment: CENTER;");

        //TODO Probablly does not work because button is disabled
        button.setFont(font);
        button.setStyle("-fx-text-fill: black; -fx-font-size: 30;");

        //handling button clicks
        button.setOnMouseClicked(event -> {
            MouseButton mouseButton = event.getButton();

            if (mouseButton == MouseButton.PRIMARY)
                //PRIMARY = LEFT.CLICK
                openCell(button);

            else if (mouseButton == MouseButton.SECONDARY)
                //SECONDARY = RIGHT.CLICK
                setFlag(button);
        });



        return  button;
    }

    ///////////////////////////////////////////////



    ///////////ACTIONS ON CELLS///////////
    private void openCell(Button button)
    {
        //make the according play
        game.executePlay(Integer.parseInt(button.getId()), MouseAction.LEFT_CLICK);

        //get cell corresponding to the button
        Cell cell = game.getBoard().getCells().get(Integer.parseInt(button.getId()));

        //if you click on a mine
        if (cell.isMined() && cell.getStatus()!= CellStatus.FLAGGED)
        {
            //this button gets red if it is a mine
            button.setBackground(new Background(new BackgroundFill(Color.RED,null, null)));
            //show all the bombs
            displayBomb(button);
            updateGrid();

        }
        //else just display the
        else if (cell.getStatus() == CellStatus.OPENED)
            updateGrid();
    }

    private void setFlag(final Button button)
    {
        //execute play
        game.executePlay(Integer.parseInt(button.getId()), MouseAction.RIGHT_CLICK);

        Point buttonPosition = getPositionFromName(Integer.parseInt(button.getId()));

        //create an image view with a flag image that will come over the button
        String imageFlagPath = "images/flag.png";
        Image image = getImage(imageFlagPath);

        ImageView imageView = createFlagImageView(image, button);

        GridPane.setConstraints(imageView, buttonPosition.x, buttonPosition.y);
        gridpane.getChildren().add(imageView);
    }

    private ImageView createFlagImageView(Image image, final Button button)
    {
        //TODO make image a little smaller then button and center it (to be done in CSS)
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(button.getHeight());
        imageView.setFitWidth(button.getWidth());

        //the flag image view has to disappear on a right click
        //nothing happens on a left click
        imageView.setOnMouseClicked(event -> {
            MouseButton mouseButton = event.getButton();

            if (mouseButton == MouseButton.SECONDARY)
            {
                //if the game has ended it should not be possilbe to modify anything
                if (!game.isHasEnded())
                {
                    //execute the play (take the flag away)
                    game.executePlay(Integer.parseInt(button.getId()), MouseAction.RIGHT_CLICK);

                    //copy the lis to avoid threading problems
                    List<Node> children = new ArrayList<>(gridpane.getChildren());

                    //run over the copy and modify the real list

                    for (Node node : children)
                    {
                        int nodeColumnIndex = GridPane.getColumnIndex(node);
                        int nodeRowIndex = GridPane.getRowIndex(node);

                        int buttonColumnIndex = GridPane.getColumnIndex(button);
                        int buttonRowIndex = GridPane.getRowIndex(button);

                        // if the the node is a imageview situated at the right spot delete it
                        if (node instanceof ImageView && nodeColumnIndex == buttonColumnIndex && nodeRowIndex == buttonRowIndex)
                        {
                            gridpane.getChildren().remove(node);
                        }
                    }
                }
            }
        });

        return imageView;
    }

    private void displayBomb(Button button)
    {

        Point buttonPosition = getPositionFromName(Integer.parseInt(button.getId()));

        Image image = getImage("images/bomb.png");

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(button.getHeight());
        imageView.setFitWidth(button.getWidth());

        GridPane.setConstraints(imageView, buttonPosition.x, buttonPosition.y);
        gridpane.getChildren().add(imageView);
    }

    //if multiple cells are changed by one action, update buttons accordingly
    private void updateGrid()
    {
        //updated each cell
        for(Cell cell : game.getBoard().getCells())
            updateCell(cell);


        if (game.isHasEnded())
        {
            newGameButton.setDisable(false);
            lockAllGrid();
            //TODO make better win/lose display
            if (game.isGameWon())
                message.setText("Game : Won");
            else // game lost
                message.setText("Game : Lost");
        }
    }

    private void lockAllGrid()
    {
        Button button;
        //find appropriated button
        //copy the list and modify the real to avoid real problems
        List<Node> children = new ArrayList<>(gridpane.getChildren());
        for (Node node : children)
        {
            //there will be imageviews and buttons in the grid
            if(node instanceof Button)
            {
                button = (Button) node;
                button.setDisable(true);
            }
        }
    }

    private void updateCell(Cell cell)
    {
        Point position = getPositionFromName(cell.getId());
        Button button = new Button();

        //find appropriated button
        for (Node node : gridpane.getChildren())
        {
            if(node instanceof Button && GridPane.getRowIndex(node) == position.y && GridPane.getColumnIndex(node) == position.x)
            {
                button = (Button) node;
                break;
            }
        }

        //update button
        if (cell.getStatus()==CellStatus.OPENED)
        {
            if (cell.isMined())
                displayBomb(button);
            //don't reveal a flagged cell
            else if (cell.getStatus()!=CellStatus.FLAGGED)
                {
                    button.setDisable(true);
                    button.setBackground(null);
                    button.setText(String.valueOf(cell.getMinedNeighbors()));
                }
        }
        //if closed nothing happens
    }
    //////////////////////////////////////

    @FXML
    Button testButton;

    @FXML
    private void test()
    {
        testButton.setFont(font);
    }


}
