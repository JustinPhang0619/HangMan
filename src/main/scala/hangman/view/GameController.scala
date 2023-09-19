package hangman.view

import scalafx.scene.text.Text
import scalafx.event.ActionEvent
import scalafx.scene.layout.FlowPane
import scalafx.scene.control.{Alert, ButtonType, TextArea}
import scalafxml.core.macros.sfxml
import hangman.MainApp
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Random

@sfxml
class GameController(private var hangmanTextArea: TextArea,
                     private var text: Text,
                     private val winStatus: Text,
                     private val realWord: Text,
                     private val buttons: FlowPane
                     ) {

  private var words: List[String] = null
  val source = Source.fromFile("src/main/resources/hangman/view/Words.txt")
  words = source.getLines().toList
  source.close()

  if (MainApp.wordData.nonEmpty) {
    val extraWords = MainApp.wordData.toList
    for (i <- extraWords.indices) {
      words = words :+ extraWords(i).wordProperty.value
    }
  }

  private var mistakes: Int = 0
  private var correct: Int = 0
  private var myWord: String = null
  private var myLetters: List[String] = List.empty
  private var answer: ArrayBuffer[String] = _


  val hangManLives: ArrayBuffer[String] = ArrayBuffer(
    """
            +---+
            |   |
                |
                |
                |
                |
       ============""",
    """
            +---+
            |   |
            O   |
                |
                |
                |
       ============""",
    """
            +---+
            |   |
            O   |
            |   |
                |
                |
       ============""",
    """
            +---+
            |   |
            O   |
           /|   |
                |
                |
       ============""",
    """
            +---+
            |   |
            O   |
           /|\  |
                |
                |
       ============""",
    """
            +---+
            |   |
            O   |
           /|\  |
           /    |
                |
       ============""",
    """
            +---+
            |   |
            O   |
           /|\  |
           / \  |
                |
       ============"""
  )

  def getRandomWord: String = {
    if (words.isEmpty) {
      println("No words found.")
      return ""
    }
    words(Random.nextInt(words.length)).toUpperCase
  }

  def initialize(): Unit = {
    mistakes = 0
    correct = 0
    myWord = getRandomWord
    myLetters = myWord.toList.map(_.toString)
    answer = ArrayBuffer.fill(myLetters.size)("_ ")
    val res = answer.mkString("")
    text.setText(res)
    winStatus.setText("")
    realWord.setText("")
    buttons.setDisable(false)

  }

  def onClick(event: ActionEvent): Unit = {
    if (myWord == null) {
      val alert = new Alert(AlertType.Error) {
        initOwner(MainApp.stage)
        title = "Error"
        headerText = "Error in clicking buttons"
        contentText = "Please start a new game"
      }.showAndWait()
      return
    }
    try {
      val button: javafx.scene.control.Button = event.source.asInstanceOf[javafx.scene.control.Button]
      val letter: String = button.getText

      var found = false
      for (i <- myLetters.indices) {
        if (myLetters(i) == letter) {
          answer(i) = letter
          found = true
          correct += 1
        }
      }
      if (found) {
        val res = answer.mkString("")
        text.setText(res)
        button.setDisable(true)
        if (correct == myWord.length) {
          winStatus.setText("You Win!")
          buttons.setDisable(true)
          handleOutcome(true)
        }
      } else {
        mistakes += 1
        if (mistakes < 6) {
          hangmanTextArea.setText(hangManLives(mistakes))
          button.setDisable(true)
        } else if (mistakes == 6) {
          hangmanTextArea.setText(hangManLives(mistakes))
          handleOutcome(false)
          winStatus.setText("You Lost!")
          realWord.setText("The word was: " + myWord)
        }
      }
    } catch {
      case e: NullPointerException =>
        val alert = new Alert(AlertType.Error) {
          initOwner(MainApp.stage)
          title = "Error"
          headerText = "Error in clicking buttons"
          contentText = "Please start a new game"
        }.showAndWait()
    }
  }

  def goHome(): Unit = {
    MainApp.showHome()
  }


  def newGame(): Unit = {
    for (i <- 0 until buttons.getChildren.size()) {
      buttons.getChildren.get(i).setDisable(false)
    }
    hangmanTextArea.editable = false
    hangmanTextArea.setText(hangManLives(0))
    initialize()
  }

  def handleOutcome(isWin: Boolean): Unit = {
    val alert = new Alert(AlertType.Confirmation) {
      initOwner(MainApp.stage)
      title = "Status"
      headerText = if (isWin) "You Won" else "You Lost"
      contentText = if (isWin) "Congratulations! You guessed the word: " + myWord + "\nDo you want to play again?"
      else "Hangman has died. The answer was: " + myWord + "\nDo you want to play again?"
    }
    buttons.setDisable(true)

    val tryAgainButtonType = new ButtonType("Try Again", ButtonData.OKDone)
    val cancelButtonType = new ButtonType("Cancel", ButtonData.CancelClose)
    alert.buttonTypes = Seq(tryAgainButtonType, cancelButtonType)

    val result = alert.showAndWait()

    result match {
      case Some(`tryAgainButtonType`) =>
        realWord.setText(if (!isWin) "The word was: " + myWord else "")
        newGame()
      case _ =>
    }

  }


}
