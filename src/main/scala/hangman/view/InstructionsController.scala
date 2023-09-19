package hangman.view

import scalafxml.core.macros.sfxml
import hangman.MainApp

@sfxml
class InstructionsController() {
  def goBack(): Unit = {
    MainApp.showHome()
  }
}